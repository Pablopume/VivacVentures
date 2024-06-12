package com.example.vivacventures.ui.rest;

import com.example.vivacventures.domain.modelo.UserWeb;
import com.example.vivacventures.domain.modelo.dto.AdminRegisterDTO;
import com.example.vivacventures.domain.modelo.dto.UserUpdateDTO;
import com.example.vivacventures.domain.servicios.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserRest {
    private final UserService userService;

    @GetMapping("/api/users")
    @Secured("ROLE_ADMIN")
    public List<UserWeb> getUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/api/users/{userId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> updateUser(@PathVariable int userId, @RequestBody UserUpdateDTO request) {
        try {
            userService.updateUser(userId, request);
            return ResponseEntity.ok("User updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user: " + e.getMessage());
        }
    }
    @DeleteMapping("/api/users/delete/{id}")
    @Secured("ROLE_ADMIN")
    public void delete(@PathVariable int id) {
        userService.delete(id);
    }




}
