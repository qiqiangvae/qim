package online.qiqiang.qim.shellclient.command;

import online.qiqiang.qim.client.ImClientService;
import online.qiqiang.qim.client.service.FriendService;
import online.qiqiang.qim.client.service.GroupService;
import online.qiqiang.qim.common.user.QimUser;
import online.qiqiang.qim.common.utils.QimResponseUtils;
import online.qiqiang.qim.common.vo.FriendAddVO;
import online.qiqiang.qim.common.vo.QimResponse;
import online.qiqiang.qim.shellclient.ShellClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
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
    private ShellClient shellClient;
    @Autowired
    private ImClientService imClientService;
    private FriendService friendService;

    @ShellMethod(value = "获取好友列表", prefix = "-")
    public List<String> fls() throws IOException {
        List<String> result = new ArrayList<>();
        Call<QimResponse<List<QimUser>>> call = friendService.allFriends(shellClient.getUserId());
        Response<QimResponse<List<QimUser>>> response = call.execute();
        if (response.isSuccessful()) {
            QimResponse<List<QimUser>> qimResponse = response.body();
            if (QimResponseUtils.isOk(qimResponse)) {
                List<QimUser> list = qimResponse.getData();
                result.add("我的好友列表：");
                if (CollectionUtils.isEmpty(list)) {
                    result.add("暂无好友");

                }
                for (QimUser user : list) {
                    result.add(String.format("\tid[%d],username[%s]", user.getUserId(), user.getUsername()));
                }
            }
        }
        return result;
    }

    @ShellMethod(key = "fadd", value = "添加好友", prefix = "-")
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

    @PostConstruct
    public void postConstruct() {
        friendService = imClientService.getService(FriendService.class);
    }

}