package online.qiqiang.qim.protocol;

import lombok.Getter;

/**
 * IM 协议
 *
 * @author qiqiang
 */
@Getter
public class ImProtocol {
    /**
     * 开始标记，4 bit
     */
    private final int startFlag = ImProtocolConst.START_FLAG;
    /**
     * 协议版本，4 bit
     */
    private int version;
    /**
     * 消息类型，4 bit
     */
    private int msgType;
    /**
     * 内容长度,4 bit
     */
    private int bodyLength;

    /**
     * 消息内容
     */
    private byte[] body;

    public void setVersion(ImProtocolVersion version) {
        this.version = version.ordinal();
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public void setBody(byte[] body) {
        if (body == null) {
            this.bodyLength = 0;
        } else {
            this.bodyLength = body.length;
        }
        this.body = body;
    }

    public void setBody(Object body){
       setBody( HessianUtils.write(body));
    }
}