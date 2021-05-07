package online.qiqiang.qim.client.im;

import online.qiqiang.qim.protocol.msg.MsgType;
import online.qiqiang.qim.protocol.msg.QimMsg;

/**
 * @author qiqiang
 */
public interface MsgReceiveCallback {
    void call(MsgType msgType, QimMsg message);
}