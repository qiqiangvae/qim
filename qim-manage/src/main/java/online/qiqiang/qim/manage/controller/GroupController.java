package online.qiqiang.qim.manage.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.qiqiang.qim.common.vo.GroupPushUserVO;
import online.qiqiang.qim.common.vo.QimResponse;
import online.qiqiang.qim.common.vo.QimUserVO;
import online.qiqiang.qim.manage.service.GroupService;
import online.qiqiang.qim.managesdk.GroupManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author qiqiang
 */
@RestController
@Api("群组管理API")
public class GroupController {

    @Autowired
    private GroupManager groupManager;
    @Autowired
    private GroupService groupService;

    @ApiOperation("获取群组成员列表")
    @GetMapping("/group/{groupId}")
    public QimResponse<List<QimUserVO>> groupMembers(@PathVariable(value = "groupId") String groupId) {
        Set<String> members = groupManager.getGroupMembers(groupId);
        List<QimUserVO> users = new ArrayList<>();
        for (String member : members) {
            QimUserVO user = new QimUserVO();
            user.setUserId(Long.parseLong(member));
            users.add(user);
        }
        return new QimResponse<>(users);
    }

    @ApiOperation(("添加成员到群组"))
    @PostMapping("/group/push")
    public QimResponse<Void> friend(@RequestBody GroupPushUserVO vo) {
        boolean ok = groupService.pushUser(vo.getGroupId(), vo.getUserIdList());
        return ok ? QimResponse.ok() : QimResponse.fail();
    }
}