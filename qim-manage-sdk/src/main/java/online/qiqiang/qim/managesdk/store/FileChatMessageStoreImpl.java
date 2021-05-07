package online.qiqiang.qim.managesdk.store;

import online.qiqiang.qim.managesdk.ChatLine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 文件存储
 *
 * @author qiqiang
 */
@Component
@ConditionalOnMissingBean(ChatMessageStore.class)
public class FileChatMessageStoreImpl implements ChatMessageStore {
    @Override
    public boolean write(ChatLine chatLine) {
        return true;
    }

    @Override
    public List<ChatLine> read(String userId) {
        return Collections.emptyList();
    }
}