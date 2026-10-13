package com.chanakya.controller;

import com.chanakya.dto.*;

import com.chanakya.entity.User;

import com.chanakya.repository.UserRepository;

import com.chanakya.security.AuthUtil;

import com.chanakya.service.ImageUploadService;

import com.chanakya.service.UserDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserDashboardController {

    private final UserRepository userRepository;
    private final UserDashboardService userDashboardService;
    private final AuthUtil authUtil;
    private final ImageUploadService imageUploadService;

    @Autowired
    UserDetailsService userDetailsService;

    @GetMapping("/dashboard")
    public ApiResponse<UserDashboardDTO> getDashboard() {

        long userId = authUtil.getCurrentUserId();

        UserDashboardDTO dashboard = userDashboardService.getDashboard(userId);

        return new ApiResponse<>(true, "Dashboard fetched successfully", dashboard);
    }

    // ✅ NEW: get profile
    @GetMapping("/profile")
    public ApiResponse<ProfileResponse> getProfile() {

        Long userId = authUtil.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new ApiResponse<>(true, "Profile fetched successfully", ProfileResponse.from(user));
    }

    // ✅ UPDATED: ab updated profile return karta hai
    @PutMapping("/profile")
    public ApiResponse<ProfileResponse> updateProfile(@RequestBody UpdateProfileRequest request) {

        Long userId = authUtil.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // naam blank na ho jaye isliye isBlank check
        if (request.getFirstName() != null && !request.getFirstName().isBlank()) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null && !request.getLastName().isBlank()) user.setLastName(request.getLastName());

        if (request.getClassOrYear() != null) user.setClassOrYear(request.getClassOrYear());
        if (request.getStream() != null) user.setStream(request.getStream());
        if (request.getInterests() != null) user.setInterests(request.getInterests());
        if (request.getStrengths() != null) user.setStrengths(request.getStrengths());
        if (request.getChallenges() != null) user.setChallenges(request.getChallenges());
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getDateOfBirth() != null) user.setDateOfBirth(request.getDateOfBirth());
        if (request.getGender() != null) user.setGender(request.getGender());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getCity() != null) user.setCity(request.getCity());
        if (request.getState() != null) user.setState(request.getState());
        if (request.getCountry() != null) user.setCountry(request.getCountry());
        if (request.getSchoolOrCollegeName() != null) user.setSchoolOrCollegeName(request.getSchoolOrCollegeName());
        if (request.getBoard() != null) user.setBoard(request.getBoard());
        if (request.getTargetExam() != null) user.setTargetExam(request.getTargetExam());
        if (request.getCareerGoal() != null) user.setCareerGoal(request.getCareerGoal());
        if (request.getLanguagesKnown() != null) user.setLanguagesKnown(request.getLanguagesKnown());

        User saved = userRepository.save(user);

        return new ApiResponse<>(true, "Profile updated successfully", ProfileResponse.from(saved));
    }

    // upload profile picture
    @PostMapping(value = "/profile/picture", consumes = "multipart/form-data")
    public ApiResponse<String> uploadProfilePicture(@RequestParam("file") MultipartFile file) throws IOException {

        Long userId = authUtil.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String imageUrl = imageUploadService.uploadProfilePicture(file, userId);

        user.setProfilePictureUrl(imageUrl);

        userRepository.save(user);

        return new ApiResponse<>(true, "Profile picture uploaded successfully", imageUrl);
    }
}