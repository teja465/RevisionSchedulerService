package com.tejaaa.RevisionSchedulerService.utils;

import com.tejaaa.RevisionSchedulerService.configs.Constants;
import com.tejaaa.RevisionSchedulerService.exceptions.InvalidParameterException;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class UserValidations {

    public static void validateUserRevisionSchedulePattern(List<Long> revisionSchedule) throws InvalidParameterException {

        // no 2  values should be same in List
        // should be in increasing order
        // should not have more than 5 numbers
        if (revisionSchedule.size() >5){
            String exceptionMessage = String.format("revision schedule pattern size must be <=5 .given size : %s",revisionSchedule.size());
            throw new InvalidParameterException(exceptionMessage);
        }
        else if (! revisionSchedule.stream().sorted().collect(Collectors.toList()).equals(revisionSchedule)){
            String exceptionMessage = String.format("revision schedule pattern should be in increasing order .Pattern : %s",revisionSchedule.toString());
            throw new InvalidParameterException(exceptionMessage);
        }
        else if (new HashSet<>(revisionSchedule).size() != revisionSchedule.size()){
            String exceptionMessage = String.format("revision schedule pattern should not have repeating values .Pattern : %s",revisionSchedule.toString());
            throw new InvalidParameterException(exceptionMessage);
        }
    }

    public static String getRandomString(int length){
        return RandomStringUtils.randomAlphabetic(length);
    }


    /**
     * @param creationDate
     * @param validTimeInSeconds
     * @return
     * timeStamp in format System.currentTimeMillis()/1000;
     * TODO:ADD docs
     */
    public static boolean isUserTokenOlderThanN(long creationDate,long validTimeInSeconds){

        long currentTimeStamp = System.currentTimeMillis()/1000;
        long diff = currentTimeStamp-creationDate;
        if (diff <= Constants.DAY_IN_SECONDS){
            return true;
        }
        return false;
    }
}
