package online.qiqiang.qim.manage.service.impl;

import online.qiqiang.qim.manage.service.ChatGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author qiqiang
 */
@Service
public class ChatGroupServiceImpl implements ChatGroupService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public int addUser(String groupId, List<String> userIdList) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return 0;
        }
        String key = "chat_group:" + groupId;
        Long add = stringRedisTemplate.opsForSet().add(key, userIdList.toArray(new String[0]));
        if (add == null) {
            return 0;
        } else {
            return (int) (long) add;
        }
    }
}