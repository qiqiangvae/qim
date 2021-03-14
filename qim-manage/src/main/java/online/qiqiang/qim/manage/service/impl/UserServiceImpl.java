package online.qiqiang.qim.manage.service.impl;

import online.qiqiang.qim.common.user.QimUser;
import online.qiqiang.qim.manage.entity.UserAccount;
import online.qiqiang.qim.manage.repository.UserAccountRepository;
import online.qiqiang.qim.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qiqiang
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public QimUser save(QimUser user) {
        UserAccount userAccount = new UserAccount();
        Long userId = (long) user.getUsername().hashCode();
        userAccount.setUserId(userId);
        userAccount.setUsername(user.getUsername());
        userAccountRepository.save(userAccount);
        user.setUserId(userId);
        return user;
    }
}