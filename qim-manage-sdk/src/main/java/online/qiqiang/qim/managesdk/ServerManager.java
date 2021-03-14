package online.qiqiang.qim.managesdk;

import java.util.Collection;
import java.util.Map;

/**
 * @author qiqiang
 */
public interface ServerManager {

    long getConnectionSize(String serverId);

    Map<Object, Object> getAllConnection(String serverId);

    void saveConnection(String serverId, String userId, String text);

    void deleteConnection(String serverId, String userId);

    void deleteAllConnection(String serverId);



}