package com.expense.tracker.mapper;

import com.expense.tracker.dto.UserDTO;
import com.expense.tracker.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(String.valueOf(user.getId()))
                .email(user.getEmail())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
    }
}
