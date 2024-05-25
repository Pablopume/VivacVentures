package com.example.vivacventures.ui.rest;

import com.example.vivacventures.data.modelo.LoginToken;
import com.example.vivacventures.domain.servicios.TokenService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenRest {
private final TokenService tokenService;
    @GetMapping("/auth/login")
    public LoginToken login(@RequestParam String username, @RequestParam String password) {
        return tokenService.doLogin(username, password);
    }

    @GetMapping("/auth/refreshToken")
    public LoginToken refreshToken(@RequestParam("refreshToken") String refreshToken) {
        String newToken = tokenService.refreshToken(refreshToken);
        return new LoginToken(newToken, refreshToken);
    }

}
