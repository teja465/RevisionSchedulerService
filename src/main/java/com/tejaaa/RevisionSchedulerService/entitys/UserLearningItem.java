package com.tejaaa.RevisionSchedulerService.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserLearningItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;
    private String username;
    private String title;

    @Column(length = 500)
    private String description;
    private LocalDate createdOn;
    private LocalDate updatedOn;
    private  int version;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<UserLearningRevisionSchedule> revisionSchedules;

}
