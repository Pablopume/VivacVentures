package com.example.vivacventures.ui.rest;

import com.example.vivacventures.data.modelo.LoginToken;
import com.example.vivacventures.domain.modelo.dto.UserRegisterDTO;
import com.example.vivacventures.domain.servicios.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CredentialsRest {
    private final UserService userService;

    @PostMapping("/auth/register")
    public void register(@RequestBody UserRegisterDTO u) {
        userService.register(u);
    }

    @GetMapping("/auth/verified")
    public String verified(@RequestParam String verifiedString) {
        return userService.autenticarse(verifiedString);
    }

    @GetMapping("/auth/resendEmail")
    public String resendVerificationLink(@RequestParam String verifiedString) {
        return userService.resendEmail(verifiedString);
    }

    @GetMapping("/auth/login")
    public LoginToken login(@RequestParam String username, @RequestParam String password) {
        return userService.doLogin(username, password);
    }
}
