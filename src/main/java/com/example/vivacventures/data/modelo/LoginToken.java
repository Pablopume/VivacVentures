package com.example.vivacventures.data.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginToken {
    private String accessToken;
    private String refreshToken;
}
