package online.qiqiang.qim.client.event;

public enum ClientEvent {
    /**
     * 连接成功
     */
    CONNECTED,
    /**
     * 断开连接
     */
    DISCONNECTED,
    /**
     * 重新连接
     */
    RECONNECT,
    /**
     * 客户端关闭
     */
    CLOSE
}
