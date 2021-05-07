package online.qiqiang.qim.client.event;

/**
 * @author qiqiang
 */
public interface ClientEventCallback {
    void callback(ClientEvent event, Object source);
}