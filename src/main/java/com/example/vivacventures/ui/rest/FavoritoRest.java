package com.example.vivacventures.ui.rest;

import com.example.vivacventures.domain.modelo.FavoritesVivacPlaces;
import com.example.vivacventures.domain.modelo.VivacPlace;
import com.example.vivacventures.domain.servicios.FavoritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FavoritoRest {

    private final FavoritoService favoritoService;

    @PostMapping("/favorito")
    @Secured("ROLE_USER")
    public void saveFavorito(@RequestParam("listaId")int id,@RequestParam("vivacId") int vivacId) {
        favoritoService.saveFavorito(id, vivacId);
    }

    @DeleteMapping("/favorito/delete")
    @Secured("ROLE_USER")
    public void deleteFavorito(@RequestParam("listaId")int listaId,@RequestParam("vivacId") int vivacId) {
        favoritoService.deleteFavorito(listaId, vivacId);
    }

    @GetMapping("/favoritos/{username}")
    @Secured("ROLE_USER")
    public List<FavoritesVivacPlaces> getFavoritos(@PathVariable("username") String username) {
        return favoritoService.getFavoritos(username);
    }



}
