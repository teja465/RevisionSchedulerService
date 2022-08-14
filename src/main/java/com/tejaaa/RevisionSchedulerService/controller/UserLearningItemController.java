package com.tejaaa.RevisionSchedulerService.controller;

import com.tejaaa.RevisionSchedulerService.entitys.UserLearningItem;
import com.tejaaa.RevisionSchedulerService.exceptions.InvalidParameterException;
import com.tejaaa.RevisionSchedulerService.exceptions.ItemAlreadyPresentException;
import com.tejaaa.RevisionSchedulerService.exceptions.ItemNotPresentException;
import com.tejaaa.RevisionSchedulerService.service.UserLearningItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserLearningItemController {
    private final String USER_LEARNING_ITEM_SAVE ="/user-learning-item";

    @Autowired
    private UserLearningItemService userLearningItemService;

    @Transactional
    @GetMapping("/user-learning-item")
    public ResponseEntity<List<UserLearningItem>> getAllUserLearningItems(){
        List<UserLearningItem> response = userLearningItemService.getAllUserLearningItems();

        return  ResponseEntity.ok(response);
    }

    @DeleteMapping("/user-learning-item/{id}")
    @ExceptionHandler(ItemAlreadyPresentException.class)
    public ResponseEntity<UserLearningItem> deleteUserLearningItem( @PathVariable Long id) throws ItemNotPresentException {
        log.info("In UserLearningItem delete controller id : {}",id);
        UserLearningItem item = userLearningItemService.findById(id);
        try{
            userLearningItemService.deleteUserLearningItem(id);
        }
        catch (ItemNotPresentException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getLocalizedMessage());
        }
        return ResponseEntity.ok(item);
    }

    @PostMapping(USER_LEARNING_ITEM_SAVE)
    public ResponseEntity<UserLearningItem> createUserLearningItem( @RequestBody UserLearningItem userLearningItem)
            throws InvalidParameterException, ItemAlreadyPresentException, URISyntaxException {
        log.info("In UserLearningItem create controller  : {}",userLearningItem);
        UserLearningItem responseItem;

        try {
            responseItem = userLearningItemService.saveUserLearningItem(userLearningItem);
        }
        catch (ItemAlreadyPresentException itemAlreadyPresentException){
            log.info("item already present ,{}",itemAlreadyPresentException.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    itemAlreadyPresentException.getMessage());
        }
        catch (InvalidParameterException invalidParameterException){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    invalidParameterException.getMessage());
        }
        URI uri  = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api"+USER_LEARNING_ITEM_SAVE).toUriString());
        return ResponseEntity.created(uri).body(responseItem);
    }

}
