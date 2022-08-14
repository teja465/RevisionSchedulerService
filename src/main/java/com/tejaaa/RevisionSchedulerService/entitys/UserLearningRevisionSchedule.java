package com.tejaaa.RevisionSchedulerService.entitys;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class UserLearningRevisionSchedule {
    // store all revision schedule reminders here for learning item

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userLearningId;

    private int revisionCount;
    private LocalDate revisionScheduleDate;
    private LocalDate createDate;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    AppUser user;
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
