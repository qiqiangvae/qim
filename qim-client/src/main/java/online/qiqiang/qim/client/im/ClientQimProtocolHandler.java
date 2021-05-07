package online.qiqiang.qim.client.im;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import online.qiqiang.qim.protocol.HessianUtils;
import online.qiqiang.qim.protocol.ImProtocol;
import online.qiqiang.qim.protocol.msg.GroupChatMsg;
import online.qiqiang.qim.protocol.msg.MsgType;
import online.qiqiang.qim.protocol.msg.PrivateChatMsg;
import online.qiqiang.qim.protocol.msg.QimMsg;

/**
 * @author qiqiang
 */
@ChannelHandler.Sharable
@Slf4j
public class ClientQimProtocolHandler extends ChannelInboundHandlerAdapter {
    private MsgReceiveCallback msgReceiveCallback;
    private final MsgReceiveCallback defaultMsgReceiveCallback = new DefaultMsgReceiveCallback();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) throws Exception {
        if (object instanceof ImProtocol) {
            ImProtocol imProtocol = (ImProtocol) object;
            int msgType = imProtocol.getMsgType();
            byte[] body = imProtocol.getBody();
            QimMsg chatMsg = null;
            MsgType type = MsgType.type(msgType);
            if (MsgType.CHAT_PRIVATE.equals(MsgType.type(msgType))) {
                chatMsg = HessianUtils.read(body, PrivateChatMsg.class);
            } else if (MsgType.CHAT_GROUP.equals(MsgType.type(msgType))) {
                chatMsg = HessianUtils.read(body, GroupChatMsg.class);
            }
            if (chatMsg != null) {
                if (msgReceiveCallback == null) {
                    defaultMsgReceiveCallback.call(type, chatMsg);
                } else {
                    msgReceiveCallback.call(type, chatMsg);
                }
            }
        } else {
            ctx.fireChannelRead(object);
        }
    }

    public void setMsgReceiveCallback(MsgReceiveCallback msgReceiveCallback) {
        this.msgReceiveCallback = msgReceiveCallback;
    }
}