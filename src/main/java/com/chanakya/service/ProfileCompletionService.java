package com.chanakya.service;



import com.chanakya.entity.User;

import org.springframework.stereotype.Service;



@Service

public class ProfileCompletionService {



   private static final int TOTAL_FIELDS = 18;



   public int calculatePercentage(User user) {

       int filledCount = 0;



       // Existing academic/interest fields

       if (isFilled(user.getClassOrYear())) filledCount++;

       if (isFilled(user.getStream())) filledCount++;

       if (isFilled(user.getInterests())) filledCount++;

       if (isFilled(user.getStrengths())) filledCount++;

       if (isFilled(user.getChallenges())) filledCount++;



       // Personal

       if (isFilled(user.getProfilePictureUrl())) filledCount++;

       if (isFilled(user.getBio())) filledCount++;

       if (user.getDateOfBirth() != null) filledCount++;

       if (isFilled(user.getGender())) filledCount++;

       if (isFilled(user.getPhoneNumber())) filledCount++;



       // Location

       if (isFilled(user.getCity())) filledCount++;

       if (isFilled(user.getState())) filledCount++;

       if (isFilled(user.getCountry())) filledCount++;



       // Academic

       if (isFilled(user.getSchoolOrCollegeName())) filledCount++;

       if (isFilled(user.getBoard())) filledCount++;

       if (isFilled(user.getTargetExam())) filledCount++;

       if (isFilled(user.getCareerGoal())) filledCount++;



       // Social/Extra

       if (isFilled(user.getLanguagesKnown())) filledCount++;



       return (filledCount * 100) / TOTAL_FIELDS;

   }



   private boolean isFilled(String value) {

       return value != null && !value.trim().isEmpty();

   }

}