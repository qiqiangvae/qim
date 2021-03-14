package online.qiqiang.qim.server.event;

import online.qiqiang.qim.common.server.ImServerInfo;
import org.springframework.context.ApplicationEvent;

/**
 * @author qiqiang
 */
public class ImServerRegisteredEvent extends ApplicationEvent {
    private ImServerInfo imServerInfo;

    public ImServerRegisteredEvent(ImServerInfo imServerInfo) {
        super(imServerInfo);
        this.imServerInfo = imServerInfo;
    }

    public ImServerInfo getImServerInfo() {
        return imServerInfo;
    }
}