package online.qiqiang.qim.manage.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import online.qiqiang.qim.common.user.QimUser;
import online.qiqiang.qim.common.vo.FriendAddVO;
import online.qiqiang.qim.common.vo.QimResponse;
import online.qiqiang.qim.manage.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiqiang
 */
@RestController
@Api("好友管理 API")
public class FriendController {

    @Autowired
    private FriendService friendService;


    @ApiOperation("新增好友")
    @PostMapping("/friend/add")
    public QimResponse<Void> friend(@RequestBody FriendAddVO vo) {
        boolean ok = friendService.addFriend(vo.getUserId(), vo.getFriends());
        return ok ? QimResponse.ok() : QimResponse.fail();
    }

    @ApiOperation("查询好友列表")
    @GetMapping("/friend/{userId}")
    public QimResponse<List<QimUser>> friend(@ApiParam("用户ID") @PathVariable(value = "userId") Long userId) {
        List<Long> friends = friendService.friends(userId);
        List<QimUser> qimUsers = new ArrayList<>(friends.size());
        for (Long friend : friends) {
            QimUser user = new QimUser();
            user.setUserId(friend);
            qimUsers.add(user);
        }
        return new QimResponse<>(qimUsers);
    }
}