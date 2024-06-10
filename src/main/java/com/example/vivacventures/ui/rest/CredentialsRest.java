package com.example.vivacventures.ui.rest;

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
    public ResponseEntity<Void>  register(@RequestBody UserRegisterDTO u) {
        userService.register(u);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/auth/verified")
    public String verified(@RequestParam String verifiedString) {
        return userService.autenticarse(verifiedString);
    }

    @GetMapping("/auth/resendEmail")
    public String resendVerificationLink(@RequestParam String verifiedString) {
        return userService.resendEmail(verifiedString);
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
}
