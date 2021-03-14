package online.qiqiang.qim.server.im.processor;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import online.qiqiang.qim.protocol.ImProtocol;
import online.qiqiang.qim.protocol.ImProtocolVersion;
import online.qiqiang.qim.protocol.msg.MsgType;
import online.qiqiang.qim.protocol.msg.PrivateChatMsg;
import online.qiqiang.qim.server.im.ImConnectionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 本服务发送私聊消息
 *
 * @author qiqiang
 */
@Component
@Processor(type = MsgType.CHAT_PRIVATE)
@Slf4j
@Order(1)
public class PrivateMessageWriteProcessor implements QimMessageProcessor<PrivateChatMsg> {
    @Autowired
    private ImConnectionHandler connectionHandler;


    @Override
    public boolean active(ProcessorContext<PrivateChatMsg> context) {
        return (boolean) context.get(ContextConst.WRITE_KEY, false);
    }

    @Override
    public boolean process(ProcessorContext<PrivateChatMsg> context) {
        PrivateChatMsg message = context.getMessage();
        String receive = message.getReceive();
        Channel receiveChannel = connectionHandler.getChannel(receive);
        ImProtocol imProtocol = new ImProtocol();
        imProtocol.setVersion(ImProtocolVersion.V1);
        imProtocol.setMsgType(MsgType.CHAT_PRIVATE.ordinal());
        imProtocol.setBody(message);
        receiveChannel.writeAndFlush(imProtocol);
        return true;
    }

}