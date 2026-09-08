package com.expense.tracker.service;

import com.expense.tracker.constant.Role;
import com.expense.tracker.entity.User;
import com.expense.tracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserProvisioningService {
    private final UserRepository userRepository;
    private final SystemCategoryPreferenceService systemCategoryPreferenceService;

    public UserProvisioningService(UserRepository userRepository, SystemCategoryPreferenceService systemCategoryPreferenceService) {
        this.userRepository = userRepository;
        this.systemCategoryPreferenceService = systemCategoryPreferenceService;
    }

    public User provisionUser(Jwt jwt) {

        User user = userRepository.save(createUser(jwt));

        systemCategoryPreferenceService.createSystemCategoryPreference(user.getId());

        return user;
    }

    private User createUser(Jwt jwt) {
        return User.builder()
                .authUserId(jwt.getSubject())
                .email(jwt.getClaimAsString("email"))
                .username(jwt.getClaimAsString("name"))
                .firstName(jwt.getClaimAsString("given_name"))
                .lastName(jwt.getClaimAsString("family_name"))
                .role(Role.ROLE_USER)
                .phone("")
                .active(true)
                .build();
    }
}
