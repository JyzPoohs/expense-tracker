package com.expense.tracker.controller;

import com.expense.tracker.dto.UserDTO;
import com.expense.tracker.mapper.UserMapper;
import com.expense.tracker.service.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final CurrentUserService currentUserService;
    private final UserMapper userMapper;

    public AuthController(CurrentUserService currentUserService, UserMapper userMapper) {
        this.currentUserService = currentUserService;
        this.userMapper = userMapper;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> me(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(userMapper.toDTO(currentUserService.getCurrentUser(jwt)));
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<String> testAdmin() {
        return ResponseEntity.ok("Admin access only");
    }
}
