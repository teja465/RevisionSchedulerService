package com.tejaaa.RevisionSchedulerService.repository;

import com.tejaaa.RevisionSchedulerService.entitys.UserLearningRevisionSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface UserLearningRevisionScheduleRepo  extends JpaRepository<UserLearningRevisionSchedule,Long> {
    UserLearningRevisionSchedule findByid(String id);

    List<UserLearningRevisionSchedule> findAll();

    List<UserLearningRevisionSchedule>findByuserLearningId(Long id);
}
