package online.qiqiang.qim.shellclient.command;

import online.qiqiang.qim.client.ImClientService;
import online.qiqiang.qim.client.service.GroupService;
import online.qiqiang.qim.common.user.QimUser;
import online.qiqiang.qim.common.utils.QimResponseUtils;
import online.qiqiang.qim.common.vo.GroupPushUserVO;
import online.qiqiang.qim.common.vo.QimResponse;
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
@ShellCommandGroup("group")
public class QimGroupCommands {
    @Autowired
    private ImClientService imClientService;
    private GroupService groupService;

    @ShellMethod(value = "获取群成员  -g [groupId]", prefix = "-")
    public void gls(String g) throws IOException {
        Call<QimResponse<List<QimUser>>> group = groupService.group(g);
        Response<QimResponse<List<QimUser>>> response = group.execute();
        if (response.isSuccessful()) {
            QimResponse<List<QimUser>> qimResponse = response.body();
            if (QimResponseUtils.isOk(qimResponse)) {
                List<QimUser> list = qimResponse.getData();
                System.out.println(g + "群成员列表：");
                if (CollectionUtils.isEmpty(list)) {
                    System.out.println("暂无成员");
                    return;
                }
                for (QimUser user : list) {
                    System.out.printf("\tid[%d],username[%s]\n", user.getUserId(), user.getUsername());
                }
            }
        }
    }

    @ShellMethod(key = "gpush", value = "添加群成员  -g [groupId] -us [userId]", prefix = "-")
    public void groupPushUser(String g, List<Long> us) throws IOException {
        GroupPushUserVO vo = new GroupPushUserVO();
        vo.setGroupId(g);
        vo.setUserIdList(us);
        Call<QimResponse<Void>> group = groupService.push(vo);
        Response<QimResponse<Void>> response = group.execute();
        if (response.isSuccessful()) {
            QimResponse<Void> qimResponse = response.body();
            if (QimResponseUtils.isOk(qimResponse)) {
                System.out.println("添加群成员成功");
            }
        }
    }

    @PostConstruct
    public void postConstruct() {
        this.groupService = imClientService.getService(GroupService.class);
    }
}