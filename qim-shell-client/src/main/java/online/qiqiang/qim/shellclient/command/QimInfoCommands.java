package online.qiqiang.qim.shellclient.command;

import online.qiqiang.qim.shellclient.LoginContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiqiang
 */
@ShellComponent
@ShellCommandGroup("info")
public class QimInfoCommands {
    @Autowired
    private LoginContext loginContext;

    @ShellMethod(value = "获取信息   info")
    public List<String> info() {
        List<String> list = new ArrayList<>();
        if (loginContext.isLogin()) {
            list.add("用户" + loginContext.getUserId() + "已登陆，服务器地址：" + loginContext.getAddress());
        } else {
            list.add("用户暂未登陆，当前身份：guest");
        }
        return list;
    }

}