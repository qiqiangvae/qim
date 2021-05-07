package online.qiqiang.qim.client.event;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author qiqiang
 */
@Slf4j
public abstract class AbstractClientEventCallback implements ClientEventCallback {
    @Override
    public void callback(ClientEvent event, Object source) {
        switch (event) {
            case CONNECTED:
                InetSocketAddress connectedSoutce = (InetSocketAddress) source;
                connected(connectedSoutce);
                break;
            case RECONNECT:
                InetSocketAddress reconnectSource = (InetSocketAddress) source;
                reconnect(reconnectSource);
                break;
            case CLOSE:
                close();
            default:
                break;
        }
    }

    protected void close() {

    }

    protected void connected(InetSocketAddress inetSocketAddress) {
        log.info("连接到服务器{}成功", inetSocketAddress);
    }

    protected void reconnect(InetSocketAddress inetSocketAddress) {
        log.warn("正在重新连接[{}]…………", inetSocketAddress);
    }
}