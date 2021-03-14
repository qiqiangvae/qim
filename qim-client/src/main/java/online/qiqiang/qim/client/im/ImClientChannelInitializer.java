package online.qiqiang.qim.client.im;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import online.qiqiang.qim.protocol.codec.ImProtocolDecoder;
import online.qiqiang.qim.protocol.codec.ImProtocolEncoder;


/**
 * @author qiqiang
 */
public class ImClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    private ClientConnectionHandler connectionHandler;
    private ClientQimProtocolHandler clientQimProtocolHandler;
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new ImProtocolDecoder());
        ch.pipeline().addLast(new ImProtocolEncoder());
        ch.pipeline().addLast(clientQimProtocolHandler);
        ch.pipeline().addLast(connectionHandler);
    }

    public void setClientQimProtocolHandler(ClientQimProtocolHandler clientQimProtocolHandler) {
        this.clientQimProtocolHandler = clientQimProtocolHandler;
    }

    public void setConnectionHandler(ClientConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }
}