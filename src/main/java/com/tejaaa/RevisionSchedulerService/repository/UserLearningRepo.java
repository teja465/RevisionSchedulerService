package com.tejaaa.RevisionSchedulerService.repository;

import com.tejaaa.RevisionSchedulerService.entitys.UserLearningItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserLearningRepo extends JpaRepository<UserLearningItem,Long> {

    Optional<UserLearningItem> findById(Long  id);
    Collection<UserLearningItem> findByUsername(String username);

    UserLearningItem findByUsernameAndTitle(String username,String title);

    List<UserLearningItem> findAll();
}
