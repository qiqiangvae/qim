package online.qiqiang.qim.shellclient.command;

import online.qiqiang.qim.shellclient.LoginContext;
import online.qiqiang.qim.shellclient.ShellClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

/**
 * @author qiqiang
 */
@ShellComponent
@ShellCommandGroup("send")
public class QimSendCommands {
    @Autowired
    private LoginContext loginContext;

    @Autowired
    private ShellClient shellClient;

    @ShellMethod(value = "发送消息    send -r[receiver] -g[groupId] -m [message]", prefix = "-")
    @ShellMethodAvailability("availability")
    public String send(String r, boolean g, String m) {
        return shellClient.send(r, g, m);
    }

    public Availability availability() {
        return loginContext.isLogin()
                ? Availability.available()
                : Availability.unavailable("用户未登陆");
    }

}