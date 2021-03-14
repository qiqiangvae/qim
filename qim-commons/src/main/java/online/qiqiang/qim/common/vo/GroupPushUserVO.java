package online.qiqiang.qim.common.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author qiqiang
 */
@Getter
@Setter
@ToString
public class GroupPushUserVO {
    private String groupId;
    private List<Long> userIdList;
}