package com.example.vivacventures.ui.rest;

import com.example.vivacventures.domain.modelo.Amigo;
import com.example.vivacventures.domain.servicios.AmigoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AmigoRest {
    private final AmigoService amigoService;

    @PutMapping("/friends/accept")
    @Secured("ROLE_USER")
    public void acceptFriendRequest(@RequestBody Amigo amigo) {
        amigoService.aceptarPeticionAmistad(amigo);
    }


    @DeleteMapping("/friends/reject/{id}")
    @Secured("ROLE_USER")
    public void rejectFriendRequest(@PathVariable int id) {
        amigoService.rechazarPeticionAmistad(id);
    }

    @PostMapping("/friends/send")
    @Secured("ROLE_USER")
    public void sendFriendRequest(@RequestBody Amigo amigo) {
        amigoService.mandarPeticionAmistad(amigo);
    }

    @DeleteMapping("/friends/delete/{id}")
    @Secured("ROLE_USER")
    public void deleteFriend(@PathVariable int id) {
        amigoService.eliminarAmigo(id);
    }

    @GetMapping("/friends")
    @Secured("ROLE_USER")
    public List<Amigo> getFriends(String username) {
        return amigoService.getAmigos(username);
    }

}
