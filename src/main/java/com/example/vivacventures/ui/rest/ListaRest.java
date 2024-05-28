package com.example.vivacventures.ui.rest;

import com.example.vivacventures.domain.modelo.Lista;
import com.example.vivacventures.domain.modelo.dto.ListaDTO;
import com.example.vivacventures.domain.servicios.ListaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ListaRest {
    private final ListaService listaService;

    @Secured("ROLE_USER")
    @PostMapping("/list")
    public void saveList(@RequestBody Lista lista) {
        listaService.saveLista(lista);
    }

    // el username del usuario al que se le compartirá la lista
    @Secured("ROLE_USER")
    @PostMapping("/list/share")
    public void shareList(@RequestParam("id") int listaId, @RequestParam("username") String username) {
        listaService.shareList(listaId, username);
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/list/delete")
    public void deleteList(@RequestParam("id") int id) {
        listaService.deleteLista(id);
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/list/delete/shared")
    public void deleteSharedList(@RequestParam("id") int id, @RequestParam("username") String username) {
        listaService.deleteSharedList(id, username);
    }

    @Secured("ROLE_USER")
    @PostMapping("/list/favorite")
    public void addFavoriteToList(@RequestParam("id") int id, @RequestParam("vivacId") int vivacId) {
        listaService.addFavoritoToLista(id, vivacId);
    }

    @Secured("ROLE_USER")
    @GetMapping("/lists")
    public List<ListaDTO> getLists(@RequestParam("username") String username) {
        return listaService.getLists(username);
    }
    @Secured("ROLE_USER")
    @GetMapping("/list/{id}")
    public ListaDTO getList(@PathVariable("id") int id) {
        return listaService.getList(id);
    }


}
