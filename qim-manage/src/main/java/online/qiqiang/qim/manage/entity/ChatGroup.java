package online.qiqiang.qim.manage.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 聊天组
 *
 * @author qiqiang
 */
@Getter
@Setter
public class ChatGroup {
    private String groupId;
    private List<String> userIdList;
}