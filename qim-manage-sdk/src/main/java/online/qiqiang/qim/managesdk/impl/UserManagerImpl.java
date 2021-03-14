package online.qiqiang.qim.managesdk.impl;

import online.qiqiang.qim.managesdk.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static online.qiqiang.qim.managesdk.ManagerConst.USER_STATUS_PREFIX;

/**
 * @author qiqiang
 */
@Service
public class UserManagerImpl implements UserManager {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void saveUserStatus(String userId, String text) {
        String key = USER_STATUS_PREFIX + userId;
        stringRedisTemplate.opsForValue().set(key, text);
    }

    @Override
    public String getUserStatus(String userId) {
        String key = USER_STATUS_PREFIX + userId;
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteUserStatus(String userId) {
        String key = USER_STATUS_PREFIX + userId;
        stringRedisTemplate.delete(key);
    }

    @Override
    public void deleteUserStatus(Collection<String> userIdList) {
        for (String userId : userIdList) {
            deleteUserStatus(userId);
        }
    }
}