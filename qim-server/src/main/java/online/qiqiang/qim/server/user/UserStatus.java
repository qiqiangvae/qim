package online.qiqiang.qim.server.user;

import lombok.Getter;
import lombok.Setter;

/**
 * @author qiqiang
 */
@Getter
@Setter
public class UserStatus {
    private String userId;
    private String connectedAddr;
    private String imServerId;
}