package online.qiqiang.qim.managesdk.impl;

import online.qiqiang.qim.managesdk.ServerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

import static online.qiqiang.qim.managesdk.ManagerConst.CONNECTION_PREFIX;

/**
 * @author qiqiang
 */
@Service
public class ServerManagerImpl implements ServerManager {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public long getConnectionSize(String serverId) {
        String hashKey = CONNECTION_PREFIX + serverId;
        return stringRedisTemplate.opsForHash().size(hashKey);
    }

    @Override
    public Map<Object, Object> getAllConnection(String serverId) {
        String hashKey = CONNECTION_PREFIX + serverId;
        return stringRedisTemplate.opsForHash().entries(hashKey);
    }

    @Override
    public void saveConnection(String serverId, String userId, String text) {
        String hashKey = CONNECTION_PREFIX + serverId;
        stringRedisTemplate.opsForHash().put(hashKey, userId, text);
    }

    @Override
    public void deleteConnection(String serverId, String userId) {
        String hashKey = CONNECTION_PREFIX + serverId;
        stringRedisTemplate.opsForHash().delete(hashKey, userId);
    }

    @Override
    public void deleteAllConnection(String serverId) {
        String hashKey = CONNECTION_PREFIX + serverId;
        stringRedisTemplate.delete(hashKey);
    }

}