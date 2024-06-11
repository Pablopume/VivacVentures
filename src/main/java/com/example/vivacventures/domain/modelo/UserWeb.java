package com.example.vivacventures.domain.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserWeb {
    private int id;
    private String username;
    private String email;
    private String rol;
    private boolean verified;
}
