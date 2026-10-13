package com.chanakya.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdateProfileRequest {
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
}
