package online.qiqiang.qim.manage.service;

import java.util.List;

/**
 * @author qiqiang
 */
public interface FriendService {
    boolean addFriend(Long userId, List<Long> friends);

    List<Long> friends(Long userId);
}