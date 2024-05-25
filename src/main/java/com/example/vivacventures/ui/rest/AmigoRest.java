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

    @PutMapping("/accept")
    @Secured("ROLE_USER")
    public void acceptFriendRequest(@RequestBody Amigo amigo) {
        amigoService.aceptarPeticionAmistad(amigo);
    }


    @DeleteMapping("/reject")
    @Secured("ROLE_USER")
    public void rejectFriendRequest(@RequestBody Amigo amigo) {
        amigoService.rechazarPeticionAmistad(amigo);
    }

    @PostMapping("/send")
    @Secured("ROLE_USER")
    public void sendFriendRequest(@RequestBody Amigo amigo) {
        amigoService.mandarPeticionAmistad(amigo);
    }

    @DeleteMapping("/delete")
    @Secured("ROLE_USER")
    public void deleteFriend(@RequestBody Amigo amigo) {
        amigoService.eliminarAmigo(amigo);
    }

    @GetMapping("/friends")
    @Secured("ROLE_USER")
    public List<Amigo> getFriends(String username) {
        return amigoService.getAmigos(username);
    }

}
