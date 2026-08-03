package com.expense.tracker.service;

import com.expense.tracker.constant.Role;
import com.expense.tracker.entity.User;
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
        User user = userRepository.findByAuthUserId(jwt.getSubject()).orElse(null);

        if(user == null) {
            return createUser(jwt);
        }
        return user;
    }

    public Long getCurrentUserId(Jwt jwt) {
        return this.getCurrentUser(jwt).getId();
    }

    private User createUser(Jwt jwt) {
        User user = User.builder()
                .authUserId(jwt.getId())
                .email(jwt.getClaimAsString("email"))
                .username(jwt.getClaimAsString("name"))
                .firstName(jwt.getClaimAsString("given_name"))
                .lastName(jwt.getClaimAsString("family_name"))
                .role(Role.ROLE_USER)
                .phone(jwt.getClaimAsString("phone"))
                .active(true)
                .build();

        return userRepository.save(user);
    }
}
