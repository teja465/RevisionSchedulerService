package com.tejaaa.RevisionSchedulerService.service;

import com.tejaaa.RevisionSchedulerService.entitys.AppUser;
import com.tejaaa.RevisionSchedulerService.entitys.UserLearningItem;
import com.tejaaa.RevisionSchedulerService.entitys.UserLearningRevisionSchedule;
import com.tejaaa.RevisionSchedulerService.exceptions.InvalidParameterException;
import com.tejaaa.RevisionSchedulerService.exceptions.ItemAlreadyPresentException;
import com.tejaaa.RevisionSchedulerService.exceptions.ItemNotPresentException;
import com.tejaaa.RevisionSchedulerService.repository.AppUserRepo;
import com.tejaaa.RevisionSchedulerService.repository.UserLearningRepo;
import com.tejaaa.RevisionSchedulerService.repository.UserLearningRevisionScheduleRepo;
import com.tejaaa.RevisionSchedulerService.utils.RevisionSchedulerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@Transactional
public class UserLearningItemServiceImpl implements UserLearningItemService{

    @Autowired
    private UserLearningRepo userLearningRepo;

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private UserLearningRevisionScheduleRepo userLearningRevisionScheduleRepo;

    @Override
    public List<UserLearningItem> getAllUserLearningItems() {
        AppUser user = appUserRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        log.info("fetching all UserLearningItem's of user {}",user.getUsername());


        List<UserLearningItem> response = (List<UserLearningItem>) userLearningRepo.findByUsername(user.getUsername());
        return response;
    }

    @Override
    public UserLearningItem findById(Long id) {
        final UserLearningItem[] item = new UserLearningItem[1];
        userLearningRepo.findById(id).ifPresent( userLearningItem -> {
            item[0] = userLearningItem;
        });
        return item[0];
    }

    @Override
    public UserLearningItem saveUserLearningItem(UserLearningItem userLearningItem) throws InvalidParameterException,ItemAlreadyPresentException {

        AppUser user = appUserRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        userLearningItem.setUsername(user.getUsername());
        log.info("saveUserLearningItem for user ,{}",user.getUsername());
        if(userLearningRepo.findByUsernameAndTitle(user.getUsername(),userLearningItem.getTitle()) != null){

            log.warn("userLearningItem with username {} and title {} already present",
                    userLearningItem.getUsername(),userLearningItem.getTitle());
            String message = String.format("userLearningItem with username '%s' and title '%s' already present",
                    user.getUsername(),userLearningItem.getTitle());
            throw new ItemAlreadyPresentException(message);
        }

        RevisionSchedulerUtil.validateUserProfileRevisionPattern(user);
        Collection<Long> revisionSchedule = RevisionSchedulerUtil.getFormattedRevisionSchedule(
                user.getUserProfile().getRevisionPattern());

        log.info(" Saving userLearningItem with username {} and title {} ",
                userLearningItem.getUsername(),userLearningItem.getTitle());

            userLearningItem = userLearningRepo.save(userLearningItem);

        int revisionCount=0;
        LocalDate scheduleDate = LocalDate.now();
        log.info("generating and saving revisions ,Count : {}",revisionSchedule.size());
        List<UserLearningRevisionSchedule> schedules = new ArrayList<UserLearningRevisionSchedule>();

        for (Long days:revisionSchedule) {
            scheduleDate=scheduleDate.plusDays(days);
            revisionCount+=1;
            log.info("saving revision {}/{}",revisionCount,revisionSchedule.size());
            UserLearningRevisionSchedule schedule = new UserLearningRevisionSchedule(null,userLearningItem.getId(),
                    revisionCount,scheduleDate,LocalDate.now(),null);
            schedules.add(schedule);
        }
        userLearningItem.setRevisionSchedules(schedules);
        return userLearningItem;
    }

    @Override
    public UserLearningItem findItemByUsernameAndTitle(String name, String title) {
        return userLearningRepo.findByUsernameAndTitle(name,title);
    }


    @Override
    public void deleteUserLearningItem(UserLearningItem item) throws ItemNotPresentException{
        log.info("Deleting UserLearningItem {} ",item);

        userLearningRepo.delete(item);
        List<UserLearningRevisionSchedule> schedules =
                userLearningRevisionScheduleRepo.findByuserLearningId(item.getId());
        for (UserLearningRevisionSchedule userLearningRevisionSchedule : schedules) {
            log.info("Deleting schedule of UserLearningItem Schedule :{}",userLearningRevisionSchedule);
            userLearningRevisionScheduleRepo.delete(userLearningRevisionSchedule);
        }
    }

    @Override
    public void deleteUserLearningItem(long id) throws ItemNotPresentException {
        Optional<UserLearningItem> item = userLearningRepo.findById(id);
        String exceptionMessage=String.format(" UserLearningItem with given id '%s' not found to delete  ",id);
        item.ifPresent(item1 -> {
                try {
                    deleteUserLearningItem(item1);
                } catch (ItemNotPresentException e) {
                    e.printStackTrace();
                }
            });

        if(item.isEmpty()){
            log.info("UserLearningItem with given id {} not present to delete  ",id);
            throw new ItemNotPresentException(exceptionMessage);
        }
    }

}
