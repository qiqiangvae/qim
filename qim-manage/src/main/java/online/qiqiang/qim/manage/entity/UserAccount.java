package online.qiqiang.qim.manage.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author qiqiang
 */
@Getter
@Setter
@Table(name = "qim_user_account")
@Entity
public class UserAccount {
    @Id
    @Column(length = 10)
    private Long userId;
    @Column(length = 10)
    private String username;
}