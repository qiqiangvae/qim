package online.qiqiang.qim.client.im;

import online.qiqiang.qim.protocol.msg.GroupChatMsg;
import online.qiqiang.qim.protocol.msg.MsgType;
import online.qiqiang.qim.protocol.msg.PrivateChatMsg;
import online.qiqiang.qim.protocol.msg.QimMsg;

/**
 * @author qiqiang
 */
public class DefaultMsgReceiveCallback implements MsgReceiveCallback {
    @Override
    public void call(MsgType msgType, QimMsg message) {
        if (MsgType.CHAT_PRIVATE.equals(msgType)) {
            PrivateChatMsg chatMsg = (PrivateChatMsg) message;
            System.out.println("[私聊]" + chatMsg.getSender() + ":" + chatMsg.getContent());
        } else if (MsgType.CHAT_GROUP.equals(msgType)) {
            GroupChatMsg chatMsg = (GroupChatMsg) message;
            System.out.println("[群聊(" + chatMsg.getGroupId() + ")]" + chatMsg.getSender() + ":" + chatMsg.getContent());
        }
    }
}