package online.qiqiang.qim.client.im;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import online.qiqiang.qim.protocol.HessianUtils;
import online.qiqiang.qim.protocol.ImProtocol;
import online.qiqiang.qim.protocol.ImProtocolVersion;
import online.qiqiang.qim.protocol.msg.ConnectionMsg;
import online.qiqiang.qim.protocol.msg.MsgType;

/**
 * @author qiqiang
 */
@ChannelHandler.Sharable
public class ClientConnectionHandler extends ChannelInboundHandlerAdapter {
    private String userId;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        ImProtocol imProtocol = new ImProtocol();
        imProtocol.setVersion(ImProtocolVersion.V1.ordinal());
        imProtocol.setMsgType(MsgType.CONNECTION.ordinal());
        ConnectionMsg chatMsg = new ConnectionMsg(userId);
        byte[] body = HessianUtils.write(chatMsg);
        imProtocol.setBody(body);
        channel.writeAndFlush(imProtocol);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}