package online.qiqiang.qim.server.im;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import online.qiqiang.qim.protocol.codec.ImProtocolDecoder;
import online.qiqiang.qim.protocol.codec.ImProtocolEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author qiqiang
 */
@Component
public class ImServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Autowired
    private ImConnectionHandler connectionHandler;
    @Autowired
    private ServerQimProtocolHandler serverQimProtocolHandler;


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new ImProtocolDecoder());
        ch.pipeline().addLast(new ImProtocolEncoder());
        ch.pipeline().addLast(connectionHandler);
        ch.pipeline().addLast(serverQimProtocolHandler);
    }

    public void close() {
        connectionHandler.close();
    }

}