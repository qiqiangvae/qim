package online.qiqiang.qim.shellclient;

import online.qiqiang.qim.shellclient.event.LoginEvent;
import online.qiqiang.qim.shellclient.event.LoginSource;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author qiqiang
 */
@Component
public class LoginContext {
    private String userId;
    private String address;

    @EventListener
    public void handle(LoginEvent event) {
        LoginSource loginSource = event.getLoginSource();
        this.userId = loginSource.getUserId();
    }

    public String getUserId() {
        return userId;
    }

    public boolean isLogin() {
        return !StringUtils.isEmpty(userId);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}