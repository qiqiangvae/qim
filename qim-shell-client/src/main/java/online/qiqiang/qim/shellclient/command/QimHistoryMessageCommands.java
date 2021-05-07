package online.qiqiang.qim.shellclient.command;

import online.qiqiang.qim.client.ImClientService;
import online.qiqiang.qim.client.service.HistoryMessageService;
import online.qiqiang.qim.common.utils.QimResponseUtils;
import online.qiqiang.qim.common.vo.ChatLineVO;
import online.qiqiang.qim.common.vo.QimResponse;
import online.qiqiang.qim.shellclient.LoginContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.util.CollectionUtils;
import retrofit2.Call;
import retrofit2.Response;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiqiang
 */
@ShellComponent
@ShellCommandGroup("history messages")
public class QimHistoryMessageCommands {
    @Autowired
    private LoginContext loginContext;
    @Autowired
    private ImClientService imClientService;

    private HistoryMessageService historyMessageService;

    @ShellMethod(value = "获取历史消息 fetch history messages")
    @ShellMethodAvailability("availability")
    public List<String> fhm() throws IOException {
        List<String> list = new ArrayList<>();
        Call<QimResponse<List<ChatLineVO>>> qimResponseCall = historyMessageService.historyMessage(Long.valueOf(loginContext.getUserId()));
        Response<QimResponse<List<ChatLineVO>>> response = qimResponseCall.execute();
        if (response.isSuccessful()) {
            QimResponse<List<ChatLineVO>> qimResponse = response.body();
            if (QimResponseUtils.isOk(qimResponse)) {
                List<ChatLineVO> data = qimResponse.getData();
                if (CollectionUtils.isEmpty(data)) {
                    list.add("暂无未读的历史消息");
                } else {
                    for (ChatLineVO datum : data) {
                        String line = datum.getTimestamp() + "[私聊]" + datum.getSender() + ":" + datum.getContent();
                        list.add(line);
                    }
                }
            }
        }
        return list;
    }

    public Availability availability() {
        return loginContext.isLogin()
                ? Availability.available()
                : Availability.unavailable("用户未登陆");
    }

    @PostConstruct
    public void postConstruct() {
        this.historyMessageService = imClientService.getService(HistoryMessageService.class);
    }

}