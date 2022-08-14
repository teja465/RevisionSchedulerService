package com.tejaaa.RevisionSchedulerService.repository;

import com.tejaaa.RevisionSchedulerService.entitys.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepo  extends JpaRepository<AppUser,Long> {

    AppUser findByUsername(String username);

}
