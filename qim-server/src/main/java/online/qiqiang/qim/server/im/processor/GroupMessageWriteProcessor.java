package online.qiqiang.qim.server.im.processor;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import online.qiqiang.qim.protocol.ImProtocol;
import online.qiqiang.qim.protocol.ImProtocolVersion;
import online.qiqiang.qim.protocol.msg.GroupChatMsg;
import online.qiqiang.qim.protocol.msg.MsgType;
import online.qiqiang.qim.server.im.ImConnectionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 本服务发送私聊消息
 *
 * @author qiqiang
 */
@Component
@Processor(MsgType.CHAT_GROUP)
@Slf4j
@Order(1)
public class GroupMessageWriteProcessor implements QimMessageProcessor<GroupChatMsg> {
    @Autowired
    private ImConnectionHandler connectionHandler;


    @Override
    public boolean active(ProcessorContext<GroupChatMsg> context) {
        return !CollectionUtils.isEmpty((Collection) context.get(ContextConst.GROUP_USERS_KEY, Collections.EMPTY_LIST));
    }

    @Override
    public boolean process(ProcessorContext<GroupChatMsg> context) {
        GroupChatMsg message = context.getMessage();
        List<String> groupUsers = (List<String>) context.get(ContextConst.GROUP_USERS_KEY, Collections.EMPTY_LIST);
        for (String groupUser : groupUsers) {
            Channel receiveChannel = connectionHandler.getChannel(groupUser);
            ImProtocol imProtocol = new ImProtocol();
            imProtocol.setVersion(ImProtocolVersion.V1);
            imProtocol.setMsgType(MsgType.CHAT_GROUP.ordinal());
            imProtocol.setBody(message);
            receiveChannel.writeAndFlush(imProtocol);
        }
        return true;
    }

}