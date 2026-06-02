package com.chanakya.controller;

import com.chanakya.dto.ApiResponse;
import com.chanakya.dto.UserDashboardDTO;
import com.chanakya.repository.UserRepository;
import com.chanakya.security.AuthUtil;
import com.chanakya.service.UserDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserDashboardController {

    private final UserRepository userRepository;
    private final UserDashboardService userDashboardService;
    private final AuthUtil authUtil;


    @Autowired
    UserDetailsService userDetailsService;

    @GetMapping("/dashboard")
    public ApiResponse<UserDashboardDTO> getDashboard() {

        long userId= authUtil.getCurrentUserId();

        UserDashboardDTO dashboard = userDashboardService.getDashboard(userId);

        return new ApiResponse<>(true, "Dashboard fetched successfully", dashboard);
    }


}