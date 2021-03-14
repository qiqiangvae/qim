package online.qiqiang.qim.protocol.msg;

/**
 * 协议消息类型
 *
 * @author qiqiang
 */
public enum MsgType {
    /**
     * 连接
     */
    CONNECTION,
    /**
     * 私聊 private chat
     */
    CHAT_PRIVATE,
    /**
     * 群聊
     */
    CHAT_GROUP;

    public static MsgType type(int type) {
        return values()[type];
    }
}