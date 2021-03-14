package online.qiqiang.qim.server.im;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.internal.PlatformDependent;
import lombok.extern.slf4j.Slf4j;
import online.qiqiang.qim.common.server.ImServerInfo;
import online.qiqiang.qim.common.utils.JsonUtils;
import online.qiqiang.qim.managesdk.ServerManager;
import online.qiqiang.qim.managesdk.UserManager;
import online.qiqiang.qim.protocol.HessianUtils;
import online.qiqiang.qim.protocol.ImProtocol;
import online.qiqiang.qim.protocol.msg.ConnectionMsg;
import online.qiqiang.qim.protocol.msg.MsgType;
import online.qiqiang.qim.server.event.ImServerRegisteredEvent;
import online.qiqiang.qim.server.user.UserStatus;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentMap;

/**
 * 连接处理器
 *
 * @author qiqiang
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class ImConnectionHandler extends ChannelInboundHandlerAdapter implements Close {
    @Resource
    private ServerManager serverManager;
    @Resource
    private UserManager userManager;
    /**
     * 服务自身信息
     */
    private ImServerInfo selfImServerInfo;

    /**
     * key channelId
     */
    private final ConcurrentMap<String, Channel> clientChannels = PlatformDependent.newConcurrentHashMap();
    /**
     * key userId , value channelId
     */
    private final ConcurrentMap<String, String> userChannelMap = PlatformDependent.newConcurrentHashMap();
    /**
     * key channelId , value userId
     */
    private final ConcurrentMap<String, String> channelUserMap = PlatformDependent.newConcurrentHashMap();


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) throws Exception {
        if (object instanceof ImProtocol) {
            ImProtocol msg = (ImProtocol) object;
            int msgType = msg.getMsgType();
            MsgType type = MsgType.type(msgType);
            if (type.equals(MsgType.CONNECTION)) {
                byte[] body = msg.getBody();
                ConnectionMsg connectionMsg = HessianUtils.read(body, ConnectionMsg.class);
                String userId = connectionMsg.getUserId();
                Channel channel = ctx.channel();
                log.info("用户{}连接到本服务器成功", userId);
                // 存储用户状态
                UserStatus userStatus = new UserStatus();
                userStatus.setUserId(userId);
                userStatus.setConnectedAddr(selfImServerInfo.getAddress());
                userStatus.setImServerId(selfImServerInfo.getId());
                String userStatusJson = JsonUtils.write(userStatus);
                userManager.saveUserStatus(userId, userStatusJson);
                // 存储客户端 channel 信息
                clientChannels.put(userId, channel);
                userChannelMap.put(userId, channel.id().asLongText());
                channelUserMap.put(channel.id().asLongText(), userId);
                serverManager.saveConnection(selfImServerInfo.getId(), userId, userStatusJson);
            } else {
                ctx.fireChannelRead(msg);
            }
        } else {
            ctx.fireChannelRead(object);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.executor().execute(() -> {
            String channelId = ctx.channel().id().asLongText();
            String userId = channelUserMap.get(channelId);
            serverManager.deleteConnection(selfImServerInfo.getId(), userId);
            userManager.deleteUserStatus(userId);
            log.info("用户{}下线", userId);
        });
    }

    public ConcurrentMap<String, Channel> getClientChannels() {
        return clientChannels;
    }

    public Channel getChannel(String userId) {
        return clientChannels.get(userId);
    }


    @EventListener(ImServerRegisteredEvent.class)
    public void imServerRegisteredEvent(ImServerRegisteredEvent event) {
        this.selfImServerInfo = event.getImServerInfo();
    }

    @Override
    public void close() {
        log.info("删除{}所有的连接信息", selfImServerInfo.getId());
        serverManager.deleteAllConnection(selfImServerInfo.getId());
        userManager.deleteUserStatus(userChannelMap.keySet());
        log.info("删除连接在{}的用户状态", selfImServerInfo.getId());
    }
}