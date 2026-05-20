package com.expense.tracker.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        return new JwtAuthenticationToken(jwt, extractAuthorities(jwt),
                jwt.getClaimAsString("preferred_username"));
    }

    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess == null) return List.of();

        List<String> roles = (List<String>) realmAccess.getOrDefault("roles", List.of());

        // Keycloak built-in roles to ignore
        Set<String> ignoredRoles = Set.of(
                "default-roles-" , "offline_access", "uma_authorization"
        );

        return roles.stream()
                .filter(r -> ignoredRoles.stream().noneMatch(r::startsWith))
                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)  // add prefix if missing
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
