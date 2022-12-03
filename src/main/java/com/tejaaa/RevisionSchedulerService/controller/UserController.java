package com.tejaaa.RevisionSchedulerService.controller;

import com.tejaaa.RevisionSchedulerService.configs.Constants;
import com.tejaaa.RevisionSchedulerService.entitys.AppUser;
import com.tejaaa.RevisionSchedulerService.exceptions.InvalidParameterException;
import com.tejaaa.RevisionSchedulerService.exceptions.ItemAlreadyPresentException;
import com.tejaaa.RevisionSchedulerService.exceptions.ItemNotPresentException;
import com.tejaaa.RevisionSchedulerService.repository.AppUserRepo;
import com.tejaaa.RevisionSchedulerService.service.AppUserService;
import com.tejaaa.RevisionSchedulerService.utils.UserValidations;
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
        log.info("Fetching user profile for email {}",userEmail);
        AppUser user = userService.getUser(userEmail);
        log.info(" user profile for use  {} is {}",userEmail,user);

        if (user == null){
            log.info("No user found with user email {}",userEmail);
            throw new  ResponseStatusException(HttpStatus.NOT_FOUND,"User with email "+userEmail+" not found");
        }
        return ResponseEntity.ok(user);
    }


    @CrossOrigin
    @PostMapping("/validate-user")
    public ResponseEntity<String> validateUserOTP(@RequestParam String userEmail ,@RequestParam String otp){

        log.info("VALIDATE_USER request ");
        AppUser user = userService.getUser(userEmail);
        if (user == null){
            String msg = String.format("User %s not found ",userEmail);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(msg);
        }
        if (user.isEnabled()){
            String msg = String.format("User %s is already enabled ",userEmail);
            return ResponseEntity.ok(msg);
        }
        if (!user.getUserToken().getToken().equals(otp.trim())){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(String.format("otp %s submitted for user %s  is invalid.Please enter correct otp "
                    ,otp,userEmail));
        }

        // check is token expired .Mark as expired if token is >1day old
        if( !UserValidations.isUserTokenOlderThanN(user.getUserToken().getCreatedOn(), Constants.DAY_IN_SECONDS)){
            return  ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(String.format("otp %s submitted for user %s  is Expired." +
                     "Please enter signup to generate new  otp "
                    , otp,userEmail));
        }
        userService.enableUser(user,true);
        return ResponseEntity.ok(String.format("Enabled user %s",userEmail));
    }
}
