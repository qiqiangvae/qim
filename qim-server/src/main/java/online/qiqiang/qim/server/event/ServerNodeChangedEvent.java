package online.qiqiang.qim.server.event;

import online.qiqiang.qim.common.server.ImServerInfo;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @author qiqiang
 */
public class ServerNodeChangedEvent extends ApplicationEvent {
    private List<ImServerInfo> serverInfoList;

    public ServerNodeChangedEvent(List<ImServerInfo> serverInfoList) {
        super(serverInfoList);
        this.serverInfoList = serverInfoList;
    }

    public List<ImServerInfo> getServerInfoList() {
        return serverInfoList;
    }
}