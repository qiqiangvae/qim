package online.qiqiang.qim.manage.service.impl;

import online.qiqiang.qim.manage.consts.QimManageConst;
import online.qiqiang.qim.manage.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author qiqiang
 */
@Service
public class FriendServiceImpl implements FriendService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean addFriend(Long userId, List<Long> friends) {
        String key = QimManageConst.FRIEND_RELATION_PREFIX + userId;
        String[] friendIdArray = friends.stream().map(String::valueOf).toArray(String[]::new);
        stringRedisTemplate.opsForSet().add(key, friendIdArray);
        return true;
    }

    @Override
    public List<Long> friends(Long userId) {
        String key = QimManageConst.FRIEND_RELATION_PREFIX + userId;
        Set<String> members = stringRedisTemplate.opsForSet().members(key);
        if (members == null) {
            return new ArrayList<>();
        }
        return members.stream().map(Long::valueOf).collect(Collectors.toList());
    }
}