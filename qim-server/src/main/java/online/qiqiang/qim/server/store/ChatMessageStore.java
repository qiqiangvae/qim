package online.qiqiang.qim.server.store;

import online.qiqiang.qim.server.im.ChatLine;

/**
 * @author qiqiang
 */
public interface ChatMessageStore {
    /**
     * 写消息
     *
     * @param chatLine
     * @return
     */
    boolean write(ChatLine chatLine);
}