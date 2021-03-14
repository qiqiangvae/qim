package online.qiqiang.qim.manage.service;

import java.util.List;

/**
 * @author qiqiang
 */
public interface GroupService {
    boolean pushUser(String groupId, List<Long> userIds);
}