package online.qiqiang.qim.manage.service.impl;

import online.qiqiang.qim.manage.service.GroupService;
import online.qiqiang.qim.managesdk.ManagerConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qiqiang
 */
@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean pushUser(String groupId, List<Long> userIds) {
        String key = ManagerConst.GROUP_MEMBER_PREFIX + groupId;
        String[] list = userIds.stream().map(String::valueOf).toArray(String[]::new);
        stringRedisTemplate.opsForSet().add(key, list);
        return true;
    }
}