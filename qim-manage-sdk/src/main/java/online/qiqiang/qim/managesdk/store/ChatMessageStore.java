package online.qiqiang.qim.managesdk.store;


import online.qiqiang.qim.managesdk.ChatLine;

import java.util.List;

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

    List<ChatLine> read(String userId);
}