package online.qiqiang.qim.shellclient.command;

import online.qiqiang.qim.client.ImClientService;
import online.qiqiang.qim.client.service.FriendService;
import online.qiqiang.qim.common.utils.QimResponseUtils;
import online.qiqiang.qim.common.vo.FriendAddVO;
import online.qiqiang.qim.common.vo.QimResponse;
import online.qiqiang.qim.common.vo.QimUserVO;
import online.qiqiang.qim.shellclient.LoginContext;
import online.qiqiang.qim.shellclient.ShellClient;
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
@ShellCommandGroup("friend")
public class QimFriendCommands {
    @Autowired
    private LoginContext loginContext;
    @Autowired
    private ShellClient shellClient;
    @Autowired
    private ImClientService imClientService;
    private FriendService friendService;

    @ShellMethod(value = "获取好友列表", prefix = "-")
    @ShellMethodAvailability("availability")
    public List<String> fls() throws IOException {
        List<String> result = new ArrayList<>();
        Call<QimResponse<List<QimUserVO>>> call = friendService.allFriends(shellClient.getUserId());
        Response<QimResponse<List<QimUserVO>>> response = call.execute();
        if (response.isSuccessful()) {
            QimResponse<List<QimUserVO>> qimResponse = response.body();
            if (QimResponseUtils.isOk(qimResponse)) {
                List<QimUserVO> list = qimResponse.getData();
                result.add("我的好友列表：");
                if (CollectionUtils.isEmpty(list)) {
                    result.add("暂无好友");

                }
                for (QimUserVO user : list) {
                    result.add(String.format("\tid[%d],username[%s]", user.getUserId(), user.getUsername()));
                }
            }
        }
        return result;
    }

    @ShellMethod(key = "fadd", value = "添加好友", prefix = "-")
    @ShellMethodAvailability("availability")
    public String addFriend(List<Long> ids) throws IOException {
        FriendAddVO friendAddVO = new FriendAddVO();
        friendAddVO.setUserId(shellClient.getUserId());
        friendAddVO.setFriends(ids);
        Call<QimResponse<Void>> call = friendService.add(friendAddVO);
        Response<QimResponse<Void>> response = call.execute();
        if (response.isSuccessful()) {
            QimResponse<Void> qimResponse = response.body();
            if (QimResponseUtils.isOk(qimResponse)) {
                return "添加好友成功";
            }
        }
        return "添加好友失败";
    }

    public Availability availability() {
        return loginContext.isLogin()
                ? Availability.available()
                : Availability.unavailable("用户未登陆");
    }

    @PostConstruct
    public void postConstruct() {
        friendService = imClientService.getService(FriendService.class);
    }

}