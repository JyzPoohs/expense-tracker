package com.expense.tracker.service;

import com.expense.tracker.constant.ErrorCode;
import com.expense.tracker.entity.User;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CurrentUserService {
    private final UserRepository userRepository;
    private final UserProvisioningService userProvisioningService;

    public CurrentUserService(UserRepository userRepository, UserProvisioningService userProvisioningService) {
        this.userRepository = userRepository;
        this.userProvisioningService = userProvisioningService;
    }

    public String getKeycloakUserId(Jwt jwt) {
        return jwt.getSubject();
    }

    public User getCurrentUser(Jwt jwt) {
        return userRepository
                .findByAuthUserId(jwt.getSubject())
                .orElseGet(() -> userProvisioningService.provisionUser(jwt));
    }

    public Long getCurrentUserId(Jwt jwt) {
        return this.getCurrentUser(jwt).getId();
    }
}
