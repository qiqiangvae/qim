package online.qiqiang.qim.manage.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.qiqiang.qim.manage.dto.QimUser;
import online.qiqiang.qim.common.vo.QimResponse;
import online.qiqiang.qim.common.vo.QimUserVO;
import online.qiqiang.qim.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiqiang
 */
@RestController
@Api("用户管理API")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation("注册用户")
    @PostMapping("/user/register")
    public QimResponse<QimUserVO> save(@RequestBody QimUser qimUser) {
        QimUser save = userService.save(qimUser);
        QimUserVO userVO = new QimUserVO();
        userVO.setUserId(save.getUserId());
        userVO.setUsername(save.getUsername());
        return new QimResponse<>(userVO);
    }
}