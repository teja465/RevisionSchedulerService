package com.tejaaa.RevisionSchedulerService.service;

import com.tejaaa.RevisionSchedulerService.entitys.AppUser;
import com.tejaaa.RevisionSchedulerService.entitys.Role;
import com.tejaaa.RevisionSchedulerService.exceptions.InvalidParameterException;
import com.tejaaa.RevisionSchedulerService.exceptions.ItemAlreadyPresentException;
import com.tejaaa.RevisionSchedulerService.exceptions.ItemNotPresentException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AppUserService extends UserDetailsService {

    AppUser saveUser( AppUser user) throws ItemAlreadyPresentException;
    Role saveRole ( Role role);
    boolean AddRoleToUser(String username, String Role);
    AppUser getUser(String  username);
    List<AppUser> getUsers();
    List<Role> getRoles();

    AppUser updateUserProfile(AppUser user) throws ItemNotPresentException, InvalidParameterException,NumberFormatException;
    void enableUser(AppUser user,boolean userState);
}
