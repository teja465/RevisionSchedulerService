package com.tejaaa.RevisionSchedulerService.service;

import com.tejaaa.RevisionSchedulerService.entitys.AppUser;
import com.tejaaa.RevisionSchedulerService.entitys.Role;
import com.tejaaa.RevisionSchedulerService.exceptions.InvalidParameterException;
import com.tejaaa.RevisionSchedulerService.exceptions.ItemAlreadyPresentException;
import com.tejaaa.RevisionSchedulerService.exceptions.ItemNotPresentException;
import com.tejaaa.RevisionSchedulerService.repository.AppUserRepo;
import com.tejaaa.RevisionSchedulerService.repository.RoleRepo;
import com.tejaaa.RevisionSchedulerService.utils.RevisionSchedulerUtil;
import com.tejaaa.RevisionSchedulerService.utils.UserValidations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserServiceImpl implements AppUserService{

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private RoleRepo roleRepo;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public AppUser saveUser(AppUser user) throws ItemAlreadyPresentException {
        if(appUserRepo.findByUsername(user.getUsername()) != null){
            String excpetionMessage = String.format("user with username: '%s' already present ",user.getUsername());
            log.error(excpetionMessage);
            throw new ItemAlreadyPresentException(excpetionMessage);
        }
        log.info("Saving user {} ",user.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return appUserRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        if(roleRepo.findByName(role.getName()) != null){
            log.error("ROle with name {} already present ",role.getName());
            return null;
        }
        log.info("Saving role {}",role.getName());
        return roleRepo.save(role);
    }

    @Override
    public boolean AddRoleToUser(String username, String role) {
        AppUser user = appUserRepo.findByUsername(username);
        if( user.getRoles().contains(role)){
            log.error("Role {} already present for username {}",role,username);
            return false;
        }
        Role newRole = roleRepo.findByName(role);
        if( newRole == null){
            log.info(" role {} not present in db to add to user {} .Creating role",role,username);
            newRole = new Role();
            newRole.setName(role);
            roleRepo.save(newRole);
            log.info(" Created role {}",role);
        }
        log.info(" Adding Role {} to username {}",role,username);
        user.getRoles().add(newRole);
        return true;
    }

    @Override
    public AppUser getUser(String username) {
        return appUserRepo.findByUsername(username);
    }

    @Override
    public List<AppUser> getUsers() {
        return appUserRepo.findAll();
    }

    @Override
    public List<Role> getRoles() {
        return roleRepo.findAll();
    }

    @Override
    public AppUser updateUserProfile(AppUser user) throws ItemNotPresentException, InvalidParameterException {
        if (appUserRepo.findByUsername(user.getUsername()) == null){
            String exceptionMessage = String.format("User  '%s'  not found to update ",user.getUsername());
            log.warn(exceptionMessage);
            throw new ItemNotPresentException(exceptionMessage);
        }
        log.info("Updating user {}",user.toString());
        AppUser UpdatedUser = appUserRepo.findByUsername(user.getUsername());
        UpdatedUser.setUserProfile(user.getUserProfile());
        try{
            UserValidations.validateUserRevisionSchedulePattern((List<Long>) RevisionSchedulerUtil.getFormattedRevisionSchedule(
                    user.getUserProfile().getRevisionPattern()));
        }
        catch (InvalidParameterException e){
            throw new InvalidParameterException(e.getLocalizedMessage());
        }
        catch (NumberFormatException e){
            log.error("Exception while verifying user revision patttern {}",e);
            throw new NumberFormatException(e.getMessage());
        }

        return appUserRepo.save(UpdatedUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser user =  appUserRepo.findByUsername(username);
        if (user == null) {
            log.error("User with username {} not found ",username);
            throw new UsernameNotFoundException("user with username "+username+" not found ");
        }

        List<SimpleGrantedAuthority> authorities =new ArrayList<SimpleGrantedAuthority>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        User user1 = new User(user.getUsername(),user.getPassword(),authorities);
        log.info("loadUserByUsername username is {}",user1.getUsername());
        return  user1;

    }
}
