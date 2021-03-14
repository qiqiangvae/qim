package online.qiqiang.qim.protocol;

/**
 * 协议版本
 *
 * @author qiqiang
 */
public enum ImProtocolVersion {
    /**
     * 协议版本
     */
    V1;

    public static ImProtocolVersion index(int index) {
        return values()[index];
    }
}