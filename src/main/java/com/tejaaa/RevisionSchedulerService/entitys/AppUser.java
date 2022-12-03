package com.tejaaa.RevisionSchedulerService.entitys;

import lombok.*;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private boolean isEnabled;

    @OneToMany(fetch= FetchType.EAGER)
    Collection<Role> roles;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private UserProfile userProfile;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<UserLearningItem> userLearningItems;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private UserToken userToken;



}
//delete  from app_user_roles;
//delete  from app_user;
//delete  from user_profile;
//delete  from user_learning;
//delete from user_learning_item;
// select * from app_user;seLECT * FROM user_profile;select * from user_learning_item;