package online.qiqiang.qim.server.im.processor;

import lombok.extern.slf4j.Slf4j;
import online.qiqiang.qim.client.im.ImClient;
import online.qiqiang.qim.common.server.ImServerInfo;
import online.qiqiang.qim.managesdk.GroupManager;
import online.qiqiang.qim.managesdk.ServerManager;
import online.qiqiang.qim.naming.NamingService;
import online.qiqiang.qim.protocol.ImProtocol;
import online.qiqiang.qim.protocol.msg.GroupChatMsg;
import online.qiqiang.qim.protocol.msg.MsgType;
import online.qiqiang.qim.server.event.ImServerRegisteredEvent;
import online.qiqiang.qim.server.im.ServerCluster;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 群聊消息分发
 *
 * @author qiqiang
 */
@Component
@Processor(type = MsgType.CHAT_GROUP)
@Slf4j
@Order(-1)
public class GroupMessageForwardProcessor implements QimMessageProcessor<GroupChatMsg> {
    @Resource
    private ServerManager serverManager;
    @Resource
    private GroupManager groupManager;
    @Autowired
    private NamingService namingService;
    @Autowired
    private ServerCluster serverCluster;
    /**
     * 服务自身信息
     */
    private ImServerInfo selfImServerInfo;


    @Override
    public boolean active(ProcessorContext<GroupChatMsg> context) {
        return true;
    }

    @Override
    public boolean process(ProcessorContext<GroupChatMsg> context) {
        GroupChatMsg message = context.getMessage();
        // 获取所有群内成员
        String groupId = message.getGroupId();
        Set<String> members = groupManager.getGroupMembers(groupId);
        Map<Object, Object> entries = serverManager.getAllConnection(selfImServerInfo.getId());
        List<String> users = new ArrayList<>(members.size() / 2);
        for (String userId : members) {
            if (entries.containsKey(userId)) {
                users.add(userId);
            }
        }
        context.put(ContextConst.GROUP_USERS_KEY, users);
        // 如果还有未处理的数据，则转发
        // todo 需要考虑离线用户的情况
        if (!message.isForwardMsg() && members.size() > users.size()) {
            message.setForward(true);
            context.getExecutor().execute(() -> {
                for (ImServerInfo imServerInfo : imServerIdList()) {
                    if (!StringUtils.equalsIgnoreCase(imServerInfo.getId(), selfImServerInfo.getId())) {
                        log.info("转发到{}", imServerInfo);
                        ImClient imClient = serverCluster.imClient(imServerInfo.getId());
                        ImProtocol protocol = (ImProtocol) context.get(ContextConst.PROTOCOL_KEY);
                        protocol.setBody(message);
                        imClient.write(protocol);
                    }
                }
            });
        }
        return true;
    }


    private List<ImServerInfo> imServerIdList() {
        return namingService.serverList();
    }

    @EventListener(ImServerRegisteredEvent.class)
    public void imServerRegisteredEvent(ImServerRegisteredEvent event) {
        this.selfImServerInfo = event.getImServerInfo();
    }
}