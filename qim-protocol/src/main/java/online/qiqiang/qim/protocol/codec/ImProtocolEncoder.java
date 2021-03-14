package online.qiqiang.qim.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import online.qiqiang.qim.protocol.ImProtocol;

/**
 * @author qiqiang
 */
public class ImProtocolEncoder extends MessageToByteEncoder<ImProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ImProtocol protocol, ByteBuf out) throws Exception {
        // 开始标记
        out.writeInt(protocol.getStartFlag());
        // 版本号
        out.writeInt(protocol.getVersion());
        // 消息类型
        out.writeInt(protocol.getMsgType());
        // 消息
        byte[] body = protocol.getBody();
        if (protocol.getBodyLength() == 0 || body == null) {
            out.writeInt(0);
        } else {
            out.writeInt(protocol.getBodyLength());
            out.writeBytes(body);
        }
    }
}