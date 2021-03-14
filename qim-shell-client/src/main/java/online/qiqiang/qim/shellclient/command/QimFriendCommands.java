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
    public void fls() throws IOException {
        Call<QimResponse<List<QimUser>>> call = friendService.allFriends(shellClient.getUserId());
        Response<QimResponse<List<QimUser>>> response = call.execute();
        if (response.isSuccessful()) {
            QimResponse<List<QimUser>> qimResponse = response.body();
            if (QimResponseUtils.isOk(qimResponse)) {
                List<QimUser> list = qimResponse.getData();
                System.out.println("我的好友列表：");
                if (CollectionUtils.isEmpty(list)) {
                    System.out.println("暂无好友");
                    return;
                }
                for (QimUser user : list) {
                    System.out.printf("\tid[%d],username[%s]\n", user.getUserId(), user.getUsername());
                }
            }
        }
    }

    @ShellMethod(key = "fadd", value = "添加好友", prefix = "-")
    public void addFriend(List<Long> ids) throws IOException {

        FriendAddVO friendAddVO = new FriendAddVO();
        friendAddVO.setUserId(shellClient.getUserId());
        friendAddVO.setFriends(ids);
        Call<QimResponse<Void>> call = friendService.add(friendAddVO);
        Response<QimResponse<Void>> response = call.execute();
        if (response.isSuccessful()) {
            QimResponse<Void> qimResponse = response.body();
            if (QimResponseUtils.isOk(qimResponse)) {
                System.out.println("成功添加好友");
            }
        }
    }

    @PostConstruct
    public void postConstruct() {
        friendService = imClientService.getService(FriendService.class);
    }

}