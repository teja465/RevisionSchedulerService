package com.tejaaa.RevisionSchedulerService.entitys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private AppUser user;
    private String revisionPattern;
}
