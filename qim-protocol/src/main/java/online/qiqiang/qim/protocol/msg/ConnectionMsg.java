package online.qiqiang.qim.protocol.msg;

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
public class ConnectionMsg implements QimMsg {
    private static final long serialVersionUID = 42L;

    private String userId;

    public ConnectionMsg(String userId) {
        this.userId = userId;
    }
}