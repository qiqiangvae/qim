package online.qiqiang.qim.naming;

import online.qiqiang.qim.common.server.ImServerInfo;

import java.util.List;

/**
 * 命名服务
 *
 * @author qiqiang
 */
public interface NamingService {
    /**
     * 获取搜有服务列表
     *
     * @return 服务列表
     */
    List<ImServerInfo> serverList();

    ImServerInfo register(ImServerInfo serverInfo, ServerNodeChangeCallback callback);

    void close();
}