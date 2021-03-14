package online.qiqiang.qim.shellclient.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author qiqiang
 */
public class LoginEvent extends ApplicationEvent {
    private LoginSource loginSource;

    public LoginEvent(LoginSource loginSource) {
        super(loginSource);
        this.loginSource = loginSource;
    }

    public LoginSource getLoginSource() {
        return loginSource;
    }
}