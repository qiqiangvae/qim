package online.qiqiang.qim.manage.service;

import java.util.List;

/**
 * @author qiqiang
 */
public interface ChatGroupService {
    /**
     * 添加用户进组
     *
     * @param groupId  groupId
     * @param userIdList userIdList
     * @return 组内成员数量
     */
    int addUser(String groupId, List<String> userIdList);
}