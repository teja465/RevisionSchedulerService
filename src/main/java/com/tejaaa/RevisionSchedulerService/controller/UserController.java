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
    @CrossOrigin
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

    @CrossOrigin
    @PostMapping("/update-user-profile")
    public ResponseEntity<AppUser> updateUserProfile(@RequestBody AppUser user)
            throws ItemNotPresentException, InvalidParameterException {
        log.info("Update user profile request for user {}",user.getUsername());
        log.info("userprofile {}",user.getUserProfile());
        try{
            AppUser userResponse = userService.updateUserProfile(user);
        }
        catch (InvalidParameterException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }

        return ResponseEntity.ok(user);
    }

    @CrossOrigin
    @GetMapping("/get-user-profile/{userEmail}")
    public ResponseEntity<AppUser> getUserProfile( @PathVariable String userEmail){
        AppUser user = userService.getUser(userEmail);
        if (user == null){
            log.info("No user found with user email {}",userEmail);
            throw new  ResponseStatusException(HttpStatus.NOT_FOUND,"User with email "+userEmail+" not found");
        }
        return ResponseEntity.ok(user);



    }
}
