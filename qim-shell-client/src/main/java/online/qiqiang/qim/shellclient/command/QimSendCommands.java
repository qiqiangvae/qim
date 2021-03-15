package online.qiqiang.qim.shellclient.command;

import online.qiqiang.qim.shellclient.ShellClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

/**
 * @author qiqiang
 */
@ShellComponent
@ShellCommandGroup("send")
public class QimSendCommands {
    @Autowired
    private ShellClient shellClient;
    @ShellMethod(value = "发送消息    send -r[receiver] -g[groupId] -m [message]", prefix = "-")
    public String send(String r, boolean g, String m) {
       return shellClient.send(r, g, m);
    }

}