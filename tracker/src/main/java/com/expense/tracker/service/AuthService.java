package com.expense.tracker.service;

import com.expense.tracker.constant.ErrorCode;
import com.expense.tracker.entity.User;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getKeycloakUserId(Jwt jwt) {
        return jwt.getSubject();
    }

    public User getCurrentUser(Jwt jwt) {
        return userRepository.findByAuthUserId(jwt.getSubject()).orElseThrow(() ->
                new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND, "User not found"));
    }
}
