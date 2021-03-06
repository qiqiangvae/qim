package online.qiqiang.qim.server.im.processor;

import lombok.extern.slf4j.Slf4j;
import online.qiqiang.qim.client.im.ImClient;
import online.qiqiang.qim.common.server.ImServerInfo;
import online.qiqiang.qim.common.utils.JsonUtils;
import online.qiqiang.qim.managesdk.ChatLine;
import online.qiqiang.qim.managesdk.UserManager;
import online.qiqiang.qim.managesdk.store.ChatMessageStore;
import online.qiqiang.qim.naming.NamingService;
import online.qiqiang.qim.protocol.ImProtocol;
import online.qiqiang.qim.protocol.msg.MsgType;
import online.qiqiang.qim.protocol.msg.PrivateChatMsg;
import online.qiqiang.qim.server.im.ServerCluster;
import online.qiqiang.qim.server.user.UserStatus;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 私聊消息分发
 *
 * @author qiqiang
 */
@Component
@Processor(MsgType.CHAT_PRIVATE)
@Slf4j
@Order(-1)
public class PrivateMessageForwardProcessor implements QimMessageProcessor<PrivateChatMsg> {
    @Autowired
    private NamingService namingService;
    @Autowired
    private ServerCluster serverCluster;
    @Resource
    private UserManager userManager;
    @Resource
    private ChatMessageStore chatMessageStore;


    @Override
    public boolean active(ProcessorContext<PrivateChatMsg> context) {
        return true;
    }

    @Override
    public boolean process(ProcessorContext<PrivateChatMsg> context) {
        PrivateChatMsg message = context.getMessage();
        String receive = message.getReceive();
        // 获取 receive 跟哪一个 im server 连接，将消息做转发
        String text = userManager.getUserStatus(receive);
        if (StringUtils.isBlank(text)) {
            storeWrite(message);
            return false;
        }
        UserStatus userStatus = JsonUtils.read(text, UserStatus.class);
        if (StringUtils.equalsIgnoreCase(userStatus.getImServerId(), serverCluster.getSelfServer().getId())) {
            // 标识可以写数据给用户
            context.put(ContextConst.WRITE_KEY, true);
            return true;
        } else if (!message.isForwardMsg()) {
            // 如果该消息不是转发过来的消息，那么可以转发
            forward(context, message, userStatus);
        }
        return false;
    }

    private void storeWrite(PrivateChatMsg message) {
        ChatLine chatLine = new ChatLine(message.getReceive());
        chatLine.setSender(message.getSender());
        chatLine.setContent(message.getContent());
        chatMessageStore.write(chatLine);
    }

    private void forward(ProcessorContext<PrivateChatMsg> context, PrivateChatMsg message, UserStatus userStatus) {
        message.setForward(true);
        context.getExecutor().execute(() -> {
            for (ImServerInfo serverInfo : imServerIdList()) {
                // 转发到接收人所连接的服务器
                if (StringUtils.equalsIgnoreCase(serverInfo.getId(), userStatus.getImServerId())) {
                    log.info("转发到{}", serverInfo);
                    ImClient imClient = serverCluster.imClient(serverInfo.getId());
                    ImProtocol protocol = (ImProtocol) context.get(ContextConst.PROTOCOL_KEY);
                    protocol.setBody(message);
                    imClient.write(protocol);
                    break;
                }
            }
        });
    }


    private List<ImServerInfo> imServerIdList() {
        return namingService.serverList();
    }
}