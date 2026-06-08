package com.expense.tracker.controller;

import com.expense.tracker.service.AuthService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/me")
    public String me(@AuthenticationPrincipal Jwt jwt) {
        return authService.getKeycloakUserId(jwt);
    }

    @GetMapping("/permissions")
    public Map<String, Object> testPermissions(Authentication authentication) {
        return Map.of(
                "principal", authentication.getName(),
                "authorities", authentication.getAuthorities().toString(),
                "isAdmin", authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String testAdmin() {
        return "Admin access only";
    }
}
