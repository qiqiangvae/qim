package online.qiqiang.qim.shellclient.command;

import online.qiqiang.qim.shellclient.ShellClient;
import online.qiqiang.qim.shellclient.event.LoginEvent;
import online.qiqiang.qim.shellclient.event.LoginSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

/**
 * @author qiqiang
 */
@ShellComponent
@ShellCommandGroup("login")
public class QimLoginCommands {
    @Autowired
    private ShellClient shellClient;

    @Autowired
    private ApplicationContext applicationContext;

    @ShellMethod(value = "登陆    login -u [uerId] -p [password]", prefix = "-")
    public void login(String u, String p) {
        if (shellClient.notLogin()) {
            shellClient.login(u, p);
            LoginSource loginSource = new LoginSource();
            loginSource.setUserId(u);
            applicationContext.publishEvent(new LoginEvent(loginSource));
        }
    }

}