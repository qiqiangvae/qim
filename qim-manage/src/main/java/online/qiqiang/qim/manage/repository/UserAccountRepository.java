package online.qiqiang.qim.manage.repository;

import online.qiqiang.qim.manage.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author qiqiang
 */
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
}