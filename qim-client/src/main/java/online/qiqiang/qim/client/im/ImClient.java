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
import online.qiqiang.qim.client.event.ClientEvent;
import online.qiqiang.qim.client.event.ClientEventCallback;
import online.qiqiang.qim.client.event.DefaultClientEventCallback;
import online.qiqiang.qim.protocol.ImProtocol;

import java.net.InetSocketAddress;

/**
 * @author qiqiang
 */
@Slf4j
public class ImClient {
    private long reconnectTime = 60 * 1000;
    private ClientRole role;
    private Channel channel;
    private Bootstrap bootstrap;
    private EventLoopGroup group;
    /**
     * 用户 id
     */
    private final String userId;
    /**
     * 连接地址
     */
    private InetSocketAddress inetSocketAddress;
    /**
     * 回调
     */
    private MsgReceiveCallback msgReceiveCallback;
    private ClientEventCallback clientEventCallback;
    /**
     * 客户端是否已关闭
     */
    private volatile boolean closed;

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
        // 消息处理
        ClientQimProtocolHandler clientQimProtocolHandler = new ClientQimProtocolHandler();
        clientQimProtocolHandler.setMsgReceiveCallback(msgReceiveCallback);
        initializer.setClientQimProtocolHandler(clientQimProtocolHandler);
        initializer.setConnectionHandler(connectionHandler);
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(initializer);
        if (clientEventCallback == null) {
            clientEventCallback = new DefaultClientEventCallback();
        }
        connect();
    }

    private void connect() {
        ChannelFuture channelFuture = bootstrap.connect(inetSocketAddress);
        channel = channelFuture.channel();
        channelFuture.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                reconnectTime = 60 * 1000;
                clientEventCallback.callback(ClientEvent.CONNECTED, inetSocketAddress);
                channel.closeFuture().addListener((ChannelFutureListener) closeFutureFuture -> {
                    if (closeFutureFuture.isSuccess()) {
                        reconnect();
                    }
                });
            } else {
                channel.eventLoop().execute(this::reconnect);
            }
        });
    }

    private void reconnect() {
        if (closed) {
            return;
        }
        try {
            long sleepTime = 2000;
            if (reconnectTime > sleepTime) {
                reconnectTime -= sleepTime;
            } else {
                reconnectTime = sleepTime;
            }
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 重新连接，todo 此时应该考虑重试 N 次后更换连接地址
        clientEventCallback.callback(ClientEvent.RECONNECT, inetSocketAddress);
        connect();
    }

    public void close() {
        closed = true;
        channel.close();
        group.shutdownGracefully();
        clientEventCallback.callback(ClientEvent.CLOSE, null);
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

    public void setMsgReceiveCallback(MsgReceiveCallback msgReceiveCallback) {
        this.msgReceiveCallback = msgReceiveCallback;
    }

    public void setClientEventCallback(ClientEventCallback clientEventCallback) {
        this.clientEventCallback = clientEventCallback;
    }
}