package com.example.vivacventures.ui.rest;

import com.example.vivacventures.domain.modelo.Lista;
import com.example.vivacventures.domain.servicios.ListaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ListaRest {
    private final ListaService listaService;

    @PostMapping("/list")
    public void saveList(@RequestBody Lista lista) {
        listaService.saveLista(lista);
    }

    // el username del usuario al que se le compartirá la lista
    @PostMapping("/list/share")
    public void shareList(@RequestParam("id") int listaId, @RequestParam("username") String username) {
        listaService.shareList(listaId, username);
    }

    @DeleteMapping("/list/delete")
    public void deleteList(@RequestParam("id") int id) {
        listaService.deleteLista(id);
    }

    @DeleteMapping("/list/delete/shared")
    public void deleteSharedList(@RequestParam("id") int id, @RequestParam("username") String username) {
        listaService.deleteSharedList(id, username);
    }

    @PostMapping("/list/favorite")
    public void addFavoriteToList(@RequestParam("id") int id, @RequestParam("favoritoId") int favoritoId) {
        listaService.addFavoritoToLista(id, favoritoId);
    }

}
