package online.qiqiang.qim.client.im;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import online.qiqiang.qim.client.ClientRole;
import online.qiqiang.qim.protocol.ImProtocol;

import java.net.InetSocketAddress;

/**
 * @author qiqiang
 */
@Slf4j
public class ImClient {
    private ClientRole role;
    private Channel channel;
    private Bootstrap bootstrap;
    private EventLoopGroup group;
    private final String userId;
    private InetSocketAddress inetSocketAddress;

    public ImClient(String userId) {
        this.userId = userId;
    }

    public boolean write(ImProtocol protocol) {
        try {
            return channel.writeAndFlush(protocol).sync().isSuccess();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void start() {
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();
        ImClientChannelInitializer initializer = new ImClientChannelInitializer();
        ClientConnectionHandler connectionHandler = new ClientConnectionHandler();
        connectionHandler.setUserId(userId);
        initializer.setClientQimProtocolHandler(new ClientQimProtocolHandler());
        initializer.setConnectionHandler(connectionHandler);
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(initializer);
        connect();
    }

    private void connect() {
        ChannelFuture channelFuture = bootstrap.connect(inetSocketAddress);
        channel = channelFuture.channel();
        channelFuture.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("连接到服务器{}成功", inetSocketAddress);
                channel.closeFuture().addListener((ChannelFutureListener) closeFutureFuture -> {
                    if (closeFutureFuture.isSuccess()) {
                        Thread.sleep(2000);
                        connect();
                    }
                });
            } else {
                Thread.sleep(2000);
                channel.eventLoop().execute(() -> {
                    log.warn("正在重新连接[{}]…………", inetSocketAddress);
                    connect();
                });
            }
        });

    }

    public void close() {
        group.shutdownGracefully();
    }

    public void setRole(ClientRole role) {
        this.role = role;
    }

    public ClientRole getRole() {
        return role;
    }

    public String getUserId() {
        return userId;
    }

    public void setInetSocketAddress(InetSocketAddress inetSocketAddress) {
        this.inetSocketAddress = inetSocketAddress;
    }
}