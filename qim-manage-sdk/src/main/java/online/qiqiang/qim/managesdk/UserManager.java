package online.qiqiang.qim.managesdk;

import java.util.Collection;

/**
 * @author qiqiang
 */
public interface UserManager {
    void saveUserStatus(String userId, String text);
    String getUserStatus(String userId);

    void deleteUserStatus(String userId);

    void deleteUserStatus(Collection<String> userIdList);
}