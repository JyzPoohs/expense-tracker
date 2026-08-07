package com.expense.tracker.service;

import com.expense.tracker.constant.ErrorCode;
import com.expense.tracker.constant.Role;
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
    private final SystemCategoryPreferenceService systemCategoryPreferenceService;


    public CurrentUserService(UserRepository userRepository, SystemCategoryPreferenceService systemCategoryPreference) {
        this.userRepository = userRepository;
        this.systemCategoryPreferenceService = systemCategoryPreference;
    }

    public String getKeycloakUserId(Jwt jwt) {
        return jwt.getSubject();
    }

    public User getCurrentUser(Jwt jwt) {
        User user = userRepository.findByAuthUserId(jwt.getSubject()).orElse(null);

        if(user == null) {
            try {
                return createUser(jwt);
            }
            catch (DataIntegrityViolationException ex) {
                return userRepository.findByAuthUserId(jwt.getSubject())
                        .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND, "User"));
            }
        }
        return user;
    }

    public Long getCurrentUserId(Jwt jwt) {
        return this.getCurrentUser(jwt).getId();
    }

    private User createUser(Jwt jwt) {
        User user = User.builder()
                .authUserId(jwt.getSubject())
                .email(jwt.getClaimAsString("email"))
                .username(jwt.getClaimAsString("name"))
                .firstName(jwt.getClaimAsString("given_name"))
                .lastName(jwt.getClaimAsString("family_name"))
                .role(Role.ROLE_USER)
                .phone("")
                .active(true)
                .build();

        userRepository.save(user);
        systemCategoryPreferenceService.createSystemCategoryPreference(user.getId());

        return user;
    }
}
