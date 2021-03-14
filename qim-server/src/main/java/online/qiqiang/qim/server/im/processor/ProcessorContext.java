package online.qiqiang.qim.server.im.processor;

import io.netty.util.concurrent.EventExecutor;
import online.qiqiang.qim.protocol.msg.QimMsg;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qiqiang
 */
public class ProcessorContext<M extends QimMsg> {
    private M message;
    private final Map<String, Object> data = new HashMap<>();
    private EventExecutor executor;

    public M getMessage() {
        return message;
    }

    public void setMessage(M message) {
        this.message = message;
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public Object get(String key, Object defaultValue) {
        return data.getOrDefault(key, defaultValue);
    }

    public EventExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(EventExecutor executor) {
        this.executor = executor;
    }
}