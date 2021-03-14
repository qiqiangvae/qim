package online.qiqiang.qim.naming;

import lombok.extern.slf4j.Slf4j;
import online.qiqiang.qim.common.server.ImServerInfo;
import online.qiqiang.qim.common.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiqiang
 */
@Slf4j
public class ZkNamingServiceImpl implements NamingService, Watcher {
    private static final String ROOT = "/qim_server";
    private ZooKeeper zooKeeper;
    private ImServerInfo latestServerInfo;
    private ServerNodeChangeCallback callback;
    private final String zkAddress;

    public ZkNamingServiceImpl(String zkAddress) {
        this.zkAddress = zkAddress;
        zooKeeper = createZooKeeper(zkAddress);
    }

    private ZooKeeper createZooKeeper(String zkAddress) {
        try {
            return new ZooKeeper(zkAddress, 5000, this);
        } catch (IOException e) {
            throw new NamingException(e);
        }
    }

    @Override
    public List<ImServerInfo> serverList() {
        try {
            List<String> children = zooKeeper.getChildren(ROOT, false);
            List<ImServerInfo> serverList = new ArrayList<>(children.size());
            for (String path : children) {
                Stat stat = new Stat();
                byte[] data = zooKeeper.getData(ROOT + "/" + path, false, stat);
                ImServerInfo imServerInfo = JsonUtils.read(data, ImServerInfo.class);
                serverList.add(imServerInfo);
            }
            return serverList;
        } catch (KeeperException | InterruptedException e) {
            throw new NamingException(e);
        }
    }

    @Override
    public ImServerInfo register(ImServerInfo serverInfo, ServerNodeChangeCallback callback) {
        String id = serverInfo.getId();
        if (StringUtils.isBlank(id)) {
            throw new NamingException("im server id不能为空");
        }
        try {
            String path = ROOT + "/" + id;
            Stat exists = zooKeeper.exists(path, false);
            if (exists != null) {
                throw new NamingException("id 为" + id + "的 im server 节点已存在");
            }
            this.callback = callback;
            String data = JsonUtils.write(serverInfo);
            zooKeeper.create(path, data.getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            log.info("服务注册到 zookeeper 成功");
            this.latestServerInfo = serverInfo;
            // 监听所有服务节点的变化
            zooKeeper.addWatch(ROOT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getType().equals(Event.EventType.NodeChildrenChanged)) {
                        ZkNamingServiceImpl.this.callback.nodeChanged(serverList(), latestServerInfo);
                    }
                }
            }, AddWatchMode.PERSISTENT);
        } catch (KeeperException | InterruptedException e) {
            throw new NamingException(e);
        }
        ZkNamingServiceImpl.this.callback.nodeChanged(serverList(), latestServerInfo);
        return serverInfo;
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getState().equals(Event.KeeperState.SyncConnected)) {
            log.info("连接 zookeeper 成功");
        } else if (event.getState().equals(Event.KeeperState.Disconnected)) {
            log.info("与 zookeeper 断开连接");
        } else if (event.getState().equals(Event.KeeperState.Expired)) {
            log.info("zookeeper 会话过期，重新注册");
            zooKeeper = createZooKeeper(zkAddress);
            if (latestServerInfo != null) {
                register(latestServerInfo, callback);
            }
        }
    }

    @Override
    public void close() {
        try {
            zooKeeper.close();
            latestServerInfo = null;
        } catch (InterruptedException e) {
            throw new NamingException(e);
        }
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }
}