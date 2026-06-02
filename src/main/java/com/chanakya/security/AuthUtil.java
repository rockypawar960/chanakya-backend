package com.chanakya.security;

import com.chanakya.entity.User;
import com.chanakya.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final UserRepository userRepository;

    public Long getCurrentUserId() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {

            throw new RuntimeException("User not authenticated");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .map(User::getId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }
}