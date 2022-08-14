package com.tejaaa.RevisionSchedulerService.controller;

import com.tejaaa.RevisionSchedulerService.entitys.AppUser;
import com.tejaaa.RevisionSchedulerService.exceptions.InvalidParameterException;
import com.tejaaa.RevisionSchedulerService.exceptions.ItemAlreadyPresentException;
import com.tejaaa.RevisionSchedulerService.exceptions.ItemNotPresentException;
import com.tejaaa.RevisionSchedulerService.service.AppUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private AppUserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getAllUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }
    @PostMapping("/signup")
    public ResponseEntity<AppUser> signUp(@RequestBody AppUser user) throws ItemAlreadyPresentException {
        log.info("Signing  up user :{} ",user.getUsername());
        AppUser userResponse;
        try{
            userResponse=userService.saveUser(user);
        }
        catch (ItemAlreadyPresentException itemAlreadyPresentException){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    itemAlreadyPresentException.getMessage());
        }
        URI uri  = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/signup").toUriString());
        return ResponseEntity.created(uri).body(userResponse);
    }

    @PutMapping("/update-user-profile")
    public ResponseEntity<AppUser> updateUserProfile(@RequestBody AppUser user)
            throws ItemNotPresentException, InvalidParameterException {
        log.info("Update user profile request for user {}",user.getUsername());
        log.info("user {} ; userprofile {}",user,user.getUserProfile());
        try{
            AppUser userResponse = userService.updateUserProfile(user);
        }
        catch (InvalidParameterException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }

        return ResponseEntity.ok(user);
    }
}
