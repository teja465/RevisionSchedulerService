package com.tejaaa.RevisionSchedulerService.utils;


import com.google.common.base.Preconditions;
import com.tejaaa.RevisionSchedulerService.entitys.AppUser;
import com.tejaaa.RevisionSchedulerService.exceptions.InvalidParameterException;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public  class RevisionSchedulerUtil {

    public static void validateUserProfileRevisionPattern(AppUser user) throws InvalidParameterException{
        if (user.getUserProfile() == null){
            throw new InvalidParameterException("User profile is empty for user ");
        }
        String revisionPattern = user.getUserProfile().getRevisionPattern();
        if(revisionPattern == null || revisionPattern.length() == 0 ){
            throw new InvalidParameterException("User revision pattern must not be null or empty for user {} "+user.getUsername());
        }
    }

    public static Collection<Long> getFormattedRevisionSchedule(String pattern)  {
        Preconditions.checkNotNull(pattern,"SchedulePattern is null");
        Collection<Long> scheduleDays = new ArrayList<>();

        if (pattern.length() == 0){
            return scheduleDays;
        }
        try {
            scheduleDays = Arrays.stream(pattern.split(",")).map(Long::valueOf).collect(Collectors.toList());
            scheduleDays.stream().forEach(RevisionSchedulerUtil::validateNumber);
        }
        catch (NumberFormatException e){
            throw new NumberFormatException(e.getMessage());
        }
        return scheduleDays;

    }

    private static void validateNumber(Long num) throws NumberFormatException {
        if (num >= 365) throw new NumberFormatException("Each number  must be < 365 :"+num);
    }


    @Test
      public void  test(){
        System.out.println(getFormattedRevisionSchedule(",,,"));

        LocalDate date = LocalDate.now();
        System.out.println(date);
        date = date.plusDays(5);
        System.out.println(date);
        date = date.plusDays(10);
        System.out.println(date);




    }



}
