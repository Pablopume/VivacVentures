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



//    @GetMapping("/getusers")
//    @Secured("ROLE_ADMIN")
//    public String getUsers(Model model) {
//        List<UserWeb> users = userService.getAllUsers();
//        model.addAttribute("users", users);
//        return "users";
//    }

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
//    @GetMapping("/cargarusers")
////    @Secured("ROLE_ADMIN")
//    public List<UserWeb> getAllUsers() {
//        return userService.getAllUsers();
//    }
//
//    @GetMapping("/getusers")
////    @Secured("ROLE_ADMIN")
//    public String moveToUsers() {
//        return "users";
//    }



//  <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
//    <script
//    src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
//    integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
//    crossorigin="anonymous"></script>
//
//    <script>
//    $(document).ready(function() {
//        var accessToken = localStorage.getItem('accessToken');
//        if (!accessToken) {
//            alert('No access token found. Please login first.');
//            window.location.href = '/vivacventures/login';
//            return;
//        }
//
//        // Verificación del rol del usuario
//        $.ajax({
//                type: 'GET',
//                url: '/vivacventures/api/validate-admin',
//                headers: {
//            'Authorization': 'Bearer ' + accessToken
//        },
//        success: function(response) {
//            if (response.role !== 'ROLE_ADMIN') {
//                alert('Access Denied. Admins only.');
//                window.location.href = '/vivacventures/login';
//                return;
//            }
//
//            // Si el usuario es administrador, se cargan los datos de usuarios
//            $.ajax({
//                    type: 'GET',
//                    url: '/vivacventures/api/users',
//                    headers: {
//                'Authorization': 'Bearer ' + accessToken
//            },
//            success: function(users) {
//                var userList = $('#userList');
//                users.forEach(function(user) {
//                    userList.append('<p>' + JSON.stringify(user) + '</p>');
//                });
//            },
//            error: function(xhr, status, error) {
//                if (xhr.status === 403 || xhr.status === 401) {
//                    alert('Unauthorized. Please login again.');
//                    localStorage.removeItem('accessToken');
//                    window.location.href = '/vivacventures/login';
//                } else {
//                    alert('Error al obtener los usuarios: ' + error);
//                }
//            }
//                    });
//        },
//        error: function(xhr, status, error) {
//            alert('Error al verificar el rol: ' + error);
//            window.location.href = '/vivacventures/login';
//        }
//            });
//    });
//    </script>



}
