package online.qiqiang.qim.managesdk.store;

import online.qiqiang.qim.common.utils.JsonUtils;
import online.qiqiang.qim.managesdk.ChatLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * redis 存储
 *
 * @author qiqiang
 */
@Component
@ConditionalOnProperty(name = "im-store.type", havingValue = "redis")
public class RedisChatMessageStoreImpl implements ChatMessageStore {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisChatMessageStoreImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean write(ChatLine chatLine) {
        ListOperations<String, String> ops = stringRedisTemplate.opsForList();
        String key = "store-msg:" + chatLine.getUserId();
        ops.leftPush(key, JsonUtils.write(chatLine));
        return true;
    }

    @Override
    public List<ChatLine> read(String userId) {
        List<String> keys = new ArrayList<>(1);
        keys.add("store-msg:" + userId);
        stringRedisTemplate.multi();
        String script = "local res =  redis.call('lrange', KEYS[1],1 - ARGV[1], -1) redis.call('LTRIM',KEYS[1],0,1 - ARGV[1]) table.insert(res, tostring(redis.call('llen',KEYS[1]))) return res";
        List<String> list = stringRedisTemplate.execute(new DefaultRedisScript<>(script, List.class), keys, "1");
        String size = list.remove(0);
        String remove = list.remove(list.size() - 1);
        System.out.println("本次取出" + size + "条记录，还有" + remove + "条数据");
        return list.stream().map(line -> JsonUtils.read(line, ChatLine.class))
                .collect(Collectors.toList());
    }
}