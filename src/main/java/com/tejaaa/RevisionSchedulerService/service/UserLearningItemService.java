package com.tejaaa.RevisionSchedulerService.service;

import com.tejaaa.RevisionSchedulerService.entitys.UserLearningItem;
import com.tejaaa.RevisionSchedulerService.exceptions.InvalidParameterException;
import com.tejaaa.RevisionSchedulerService.exceptions.ItemAlreadyPresentException;
import com.tejaaa.RevisionSchedulerService.exceptions.ItemNotPresentException;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserLearningItemService  {

     List<UserLearningItem> getAllUserLearningItems();
     UserLearningItem findById(Long id);
     UserLearningItem saveUserLearningItem(UserLearningItem userLearningItem) throws InvalidParameterException, ItemAlreadyPresentException;

     UserLearningItem findItemByUsernameAndTitle(String name,String title);
     void deleteUserLearningItem(UserLearningItem item) throws ItemNotPresentException;
     void deleteUserLearningItem(long id) throws ItemNotPresentException;

}
