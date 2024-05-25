package com.example.vivacventures.ui.rest;

import com.example.vivacventures.data.modelo.LoginToken;
import com.example.vivacventures.domain.modelo.dto.UserAmigoDTO;
import com.example.vivacventures.domain.modelo.dto.UserRegisterDTO;
import com.example.vivacventures.domain.servicios.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/auth/refreshToken")
    public LoginToken refreshToken(@RequestParam("refreshToken") String refreshToken) {
        String newToken = userService.refreshToken(refreshToken);
        return new LoginToken(newToken, refreshToken);

    }

    @GetMapping("/amigo")
    public UserAmigoDTO getAmigos(@RequestParam String username) {
        return userService.getUserAmigo(username);
    }

    @PutMapping("/auth/forgotPassword")
    public ResponseEntity<Void> forgotPassword(@RequestParam String email) {
        userService.forgotPassword(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/auth/resetPassword")
    public ResponseEntity<Void> resetPassword(@RequestParam String email, @RequestParam String newPassword, @RequestParam String temporalPassword) {
        userService.changePassword(email, newPassword,temporalPassword);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/auth/login")
    public LoginToken login(@RequestParam String username, @RequestParam String password) {
        return userService.doLogin(username, password);
    }
}
