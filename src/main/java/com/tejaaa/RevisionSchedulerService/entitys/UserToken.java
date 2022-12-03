package com.tejaaa.RevisionSchedulerService.entitys;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private AppUser user;
    private String token;
    /*Unix timestamp in seconds*/
    private long createdOn;
    private long tokenValidtyTimePeriodInSeconds;

}
