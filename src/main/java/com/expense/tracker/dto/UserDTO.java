package com.expense.tracker.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private String role;
}
