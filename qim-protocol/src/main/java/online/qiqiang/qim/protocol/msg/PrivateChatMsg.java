package online.qiqiang.qim.protocol.msg;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 私聊
 *
 * @author qiqiang
 */
@Getter
@Setter
@ToString
public class PrivateChatMsg implements QimMsg, ForwardMsg {
    private static final long serialVersionUID = 42L;

    private String sender;
    private String receive;
    private String content;
    @Getter(AccessLevel.NONE)
    private boolean forward;

    public PrivateChatMsg(String sender, String receive) {
        this.sender = sender;
        this.receive = receive;
    }

    @Override
    public boolean isForwardMsg() {
        return forward;
    }
}