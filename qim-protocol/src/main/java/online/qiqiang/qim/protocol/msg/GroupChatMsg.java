package online.qiqiang.qim.protocol.msg;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * 私聊
 *
 * @author qiqiang
 */
@Getter
@Setter
public class GroupChatMsg implements QimMsg, ForwardMsg {
    private static final long serialVersionUID = 42L;

    private String sender;
    private String groupId;
    private String content;
    /**
     * 是否为转发消息
     */
    @Getter(AccessLevel.NONE)
    private boolean forward;

    public GroupChatMsg(String sender, String groupId) {
        this.sender = sender;
        this.groupId = groupId;
    }


    @Override
    public boolean isForwardMsg() {
        return forward;
    }
}