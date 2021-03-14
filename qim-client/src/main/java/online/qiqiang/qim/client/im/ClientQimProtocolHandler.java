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

/**
 * @author qiqiang
 */
@ChannelHandler.Sharable
@Slf4j
public class ClientQimProtocolHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) throws Exception {
        if (object instanceof ImProtocol) {
            ImProtocol imProtocol = (ImProtocol) object;
            int msgType = imProtocol.getMsgType();
            byte[] body = imProtocol.getBody();
            if (MsgType.CHAT_PRIVATE.equals(MsgType.type(msgType))) {
                PrivateChatMsg chatMsg = HessianUtils.read(body, PrivateChatMsg.class);
                System.out.println();
                System.out.println("[私聊消息]" + chatMsg.getSender() + ":" + chatMsg.getContent());
            } else if (MsgType.CHAT_GROUP.equals(MsgType.type(msgType))) {
                GroupChatMsg chatMsg = HessianUtils.read(body, GroupChatMsg.class);
                System.out.println();
                System.out.println("[群聊(" + chatMsg.getGroupId() + ")消息]" + chatMsg.getSender() + ":" + chatMsg.getContent());
            }
        } else {
            ctx.fireChannelRead(object);
        }
    }
}