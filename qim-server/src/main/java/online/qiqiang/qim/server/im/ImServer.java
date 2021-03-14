package online.qiqiang.qim.server.im;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import online.qiqiang.qim.common.server.ImServerInfo;
import online.qiqiang.qim.naming.NamingService;
import online.qiqiang.qim.naming.ServerNodeChangeCallback;
import online.qiqiang.qim.server.event.ImServerRegisteredEvent;
import online.qiqiang.qim.server.event.ServerNodeChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.List;

/**
 * @author qiqiang
 */
@Component("imServer")
@Slf4j
public class ImServer implements ApplicationRunner {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ImServerConfiguration imServerConfiguration;
    @Autowired
    private ImServerChannelInitializer imServerChannelInitializer;
    @Autowired
    private NamingService namingService;
    private volatile boolean destroyed;

    private EventLoopGroup boss;
    private EventLoopGroup worker;

    public void start() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        boss = new NioEventLoopGroup(1);
        worker = new NioEventLoopGroup();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(imServerChannelInitializer);
        int port = imServerConfiguration.getPort();
        ChannelFuture channelFuture = serverBootstrap.bind(port);
        channelFuture.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("IM 服务端启动成功，id[{}]端口[{}]", imServerConfiguration.getId(), port);
                register();
            } else {
                log.info("IM 服务端启动失败，id[{}]端口[{}]", imServerConfiguration.getId(), port);
            }
        });
        channelFuture.channel().closeFuture().addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                destroy();
            }
        });
    }

    /**
     * 向 naming 服务注册
     */
    private void register() {
        ImServerInfo serverInfo = new ImServerInfo();
        serverInfo.setId(imServerConfiguration.getId());
        serverInfo.setAddress(imServerConfiguration.getAddress());
        namingService.register(serverInfo, new ServerNodeChangeCallback() {
            @Override
            public void nodeChanged(List<ImServerInfo> serverInfoList, ImServerInfo self) {
                log.info("im server 节点发生变动");
                applicationContext.publishEvent(new ImServerRegisteredEvent(self));
                applicationContext.publishEvent(new ServerNodeChangedEvent(serverInfoList));
            }
        });
    }

    @PreDestroy
    public void destroy() {
        if (!destroyed) {
            namingService.close();
            imServerChannelInitializer.close();
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            log.info("IM 服务端关闭成功");
            destroyed = true;
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        start();
    }
}