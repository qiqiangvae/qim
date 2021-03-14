package online.qiqiang.qim.server.im;

import online.qiqiang.qim.client.ClientRole;
import online.qiqiang.qim.client.im.ImClient;
import online.qiqiang.qim.common.server.ImServerInfo;
import online.qiqiang.qim.server.event.ImServerRegisteredEvent;
import online.qiqiang.qim.server.event.ServerNodeChangedEvent;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qiqiang
 */
@Component
@DependsOn("imServer")
public class ServerCluster {
    private List<ImServerInfo> serverInfoList;
    private ImServerInfo selfServer;
    private final Map<String, ImClient> clientMap = new ConcurrentHashMap<>();

    /**
     * 重新建立连接
     */
    private void reBuildConnected() {
        if (selfServer == null) {
            return;
        }
        for (ImServerInfo imServerInfo : serverInfoList) {
            if (imServerInfo.getId().equals(selfServer.getId())) {
                continue;
            }
            newConnection(imServerInfo);
        }
    }

    private void newConnection(ImServerInfo imServerInfo) {
        String address = imServerInfo.getAddress();
        String[] split = address.split(":");
        ImClient imClient = new ImClient(selfServer.getId());
        InetSocketAddress inetSocketAddress = new InetSocketAddress(split[0], Integer.parseInt(split[1]));
        imClient.setInetSocketAddress(inetSocketAddress);
        imClient.setRole(ClientRole.Server);
        imClient.start();
        clientMap.put(imServerInfo.getId(), imClient);
    }

    public ImClient imClient(String id) {
        return clientMap.get(id);
    }

    @EventListener(ServerNodeChangedEvent.class)
    public void imServerRegisteredEvent(ServerNodeChangedEvent event) {
        this.serverInfoList = event.getServerInfoList();
        this.reBuildConnected();
    }

    @EventListener(ImServerRegisteredEvent.class)
    public void imServerRegisteredEvent(ImServerRegisteredEvent event) {
        this.selfServer = event.getImServerInfo();
    }
}