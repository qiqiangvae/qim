package online.qiqiang.qim.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import online.qiqiang.qim.protocol.ImProtocol;
import online.qiqiang.qim.protocol.ImProtocolConst;
import online.qiqiang.qim.protocol.ImProtocolException;

import java.util.List;

/**
 * @author qiqiang
 */
public class ImProtocolDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        try {
            // startFlag + version + msgType + bodyLength = 16
            if (byteBuf.readableBytes() < 16) {
                return;
            }
            int begin;
            // 在循环中找到协议开始的位置
            while (true) {
                // 本次协议包开始的位置
                begin = byteBuf.readerIndex();
                // 标记本次协议包开始的位置
                byteBuf.markReaderIndex();
                if (byteBuf.readInt() == ImProtocolConst.START_FLAG) {
                    break;
                }
                // 没有读到 START_FLAG，那么就读取下一个字节
                byteBuf.resetReaderIndex();
                byteBuf.readByte();
            }
            // 协议内容长度
            int version = byteBuf.readInt();
            int msgType = byteBuf.readInt();
            int bodyLength = byteBuf.readInt();
            // 协议包内容数据还未到齐，回到协议开始的位置，等待数据到齐
            if (byteBuf.readableBytes() < bodyLength) {
                byteBuf.readerIndex(begin);
                return;
            }
            // 读取协议包内容数据
            byte[] body = new byte[bodyLength];
            byteBuf.readBytes(body);
            // 封装协议
            ImProtocol imProtocol = new ImProtocol();
            imProtocol.setVersion(version);
            imProtocol.setMsgType(msgType);
            imProtocol.setBody(body);
            out.add(imProtocol);
        } catch (Exception e) {
            throw new ImProtocolException(e);
        }
    }
}