package com.chanakya.dto;

import com.chanakya.entity.User;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ProfileResponse {
    private String email;
    private String firstName;
    private String lastName;
    private String classOrYear;
    private String stream;
    private String interests;
    private String strengths;
    private String challenges;
    private String bio;
    private LocalDate dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String city;
    private String state;
    private String country;
    private String schoolOrCollegeName;
    private String board;
    private String targetExam;
    private String careerGoal;
    private String languagesKnown;
    private String profilePictureUrl;

    public static ProfileResponse from(User u) {
        ProfileResponse r = new ProfileResponse();
        r.setEmail(u.getEmail());
        r.setFirstName(u.getFirstName());
        r.setLastName(u.getLastName());
        r.setClassOrYear(u.getClassOrYear());
        r.setStream(u.getStream());
        r.setInterests(u.getInterests());
        r.setStrengths(u.getStrengths());
        r.setChallenges(u.getChallenges());
        r.setBio(u.getBio());
        r.setDateOfBirth(u.getDateOfBirth());
        r.setGender(u.getGender());
        r.setPhoneNumber(u.getPhoneNumber());
        r.setCity(u.getCity());
        r.setState(u.getState());
        r.setCountry(u.getCountry());
        r.setSchoolOrCollegeName(u.getSchoolOrCollegeName());
        r.setBoard(u.getBoard());
        r.setTargetExam(u.getTargetExam());
        r.setCareerGoal(u.getCareerGoal());
        r.setLanguagesKnown(u.getLanguagesKnown());
        r.setProfilePictureUrl(u.getProfilePictureUrl());
        return r;
    }
}