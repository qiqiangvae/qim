package online.qiqiang.qim.managesdk.impl;

import online.qiqiang.qim.managesdk.GroupManager;
import online.qiqiang.qim.managesdk.ManagerConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

/**
 * @author qiqiang
 */
@Service
public class GroupManagerImpl implements GroupManager {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Set<String> getGroupMembers(String groupId) {
        String key = ManagerConst.GROUP_MEMBER_PREFIX + groupId;
        Set<String> members = stringRedisTemplate.opsForSet().members(key);
        if (members == null) {
            members = Collections.emptySet();
        }
        return members;
    }
}