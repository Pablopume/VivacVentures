package com.example.vivacventures.web.domain.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryDTO {
    private Long id;
    private String username;
    private String email;
    private String role;

    // Getters and Setters (omitted for brevity)
}