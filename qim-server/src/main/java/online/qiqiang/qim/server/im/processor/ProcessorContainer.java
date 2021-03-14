package online.qiqiang.qim.server.im.processor;

import io.netty.util.concurrent.EventExecutor;
import lombok.Getter;
import lombok.Setter;
import online.qiqiang.qim.protocol.HessianUtils;
import online.qiqiang.qim.protocol.ImProtocol;
import online.qiqiang.qim.protocol.ImProtocolException;
import online.qiqiang.qim.protocol.msg.GroupChatMsg;
import online.qiqiang.qim.protocol.msg.MsgType;
import online.qiqiang.qim.protocol.msg.PrivateChatMsg;
import online.qiqiang.qim.protocol.msg.QimMsg;

import java.util.LinkedList;
import java.util.List;

/**
 * @author qiqiang
 */
@Getter
@Setter
public class ProcessorContainer {
    private MsgType msgType;
    private List<QimMessageProcessor> handlers;

    public ProcessorContainer(MsgType msgType) {
        this.msgType = msgType;
        this.handlers = new LinkedList<>();
    }

    public void process(ImProtocol protocol, EventExecutor eventExecutor) {
        ProcessorContext context = new ProcessorContext();
        context.setExecutor(eventExecutor);
        context.put(ContextConst.PROTOCOL_KEY, protocol);
        context.setMessage(convertMsg(protocol));
        for (QimMessageProcessor handler : handlers) {
            if (handler.active(context) && !handler.process(context)) {
                break;
            }
        }
    }

    private QimMsg convertMsg(ImProtocol protocol) {
        byte[] body = protocol.getBody();
        switch (msgType) {
            case CHAT_GROUP:
                return HessianUtils.read(body, GroupChatMsg.class);
            case CHAT_PRIVATE:
                return HessianUtils.read(body, PrivateChatMsg.class);
            default:
                throw new ImProtocolException("不支持的消息");
        }
    }
}