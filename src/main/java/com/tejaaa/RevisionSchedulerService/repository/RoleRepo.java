package com.tejaaa.RevisionSchedulerService.repository;

import com.tejaaa.RevisionSchedulerService.entitys.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface RoleRepo  extends JpaRepository<Role,Long> {

    Role findByName(String  name);
}
