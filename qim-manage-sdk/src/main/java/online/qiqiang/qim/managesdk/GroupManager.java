package online.qiqiang.qim.managesdk;

import java.util.Set;

/**
 * @author qiqiang
 */
public interface GroupManager {

    Set<String> getGroupMembers(String groupId);
}