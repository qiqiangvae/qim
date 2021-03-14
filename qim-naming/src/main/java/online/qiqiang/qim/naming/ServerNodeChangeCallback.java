package online.qiqiang.qim.naming;

import online.qiqiang.qim.common.server.ImServerInfo;

import java.util.List;

/**
 * @author qiqiang
 */
@FunctionalInterface
public interface ServerNodeChangeCallback {
    /**
     * 节点发生变化
     *
     * @param serverInfoList 剩下的节点
     */
    void nodeChanged(List<ImServerInfo> serverInfoList, ImServerInfo self);
}