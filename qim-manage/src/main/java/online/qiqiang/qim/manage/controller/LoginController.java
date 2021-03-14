package online.qiqiang.qim.manage.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.qiqiang.qim.common.server.ImServerInfo;
import online.qiqiang.qim.common.vo.QimResponse;
import online.qiqiang.qim.managesdk.ServerManager;
import online.qiqiang.qim.naming.NamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qiqiang
 */
@RestController
@Api("登陆")
public class LoginController {

    @Autowired
    private NamingService namingService;
    @Resource
    private ServerManager serverManager;

    @ApiOperation("登陆")
    @PostMapping("login")
    public QimResponse<ImServerInfo> login(String userId, String password) {
        // todo 校验
        List<ImServerInfo> serverInfoList = namingService.serverList();
        ImServerInfo info = null;
        long miniSize = -1;
        for (ImServerInfo imServerInfo : serverInfoList) {
            if (info == null) {
                info = imServerInfo;
            }
            long size = serverManager.getConnectionSize(imServerInfo.getId());
            if (miniSize < 0) {
                miniSize = size;
            } else {
                if (size < miniSize) {
                    info = imServerInfo;
                    miniSize = size;
                }
            }
        }
        return new QimResponse<>(info);
    }
}