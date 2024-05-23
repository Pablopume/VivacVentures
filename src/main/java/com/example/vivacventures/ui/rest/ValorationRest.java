package com.example.vivacventures.ui.rest;

import com.example.vivacventures.domain.modelo.FavoritesVivacPlaces;
import com.example.vivacventures.domain.modelo.Valoration;
import com.example.vivacventures.domain.modelo.VivacPlace;
import com.example.vivacventures.domain.servicios.ValorationService;
import com.example.vivacventures.domain.servicios.VivacPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ValorationRest {
    private final ValorationService valorationService;

    @PostMapping("/valoration")
    @Secured("ROLE_USER")
    public void addValoration(@RequestBody Valoration valoration) {
        valorationService.saveValoration(valoration);
    }

    @DeleteMapping("/valoration/{id}")
    @Secured("ROLE_USER")
    public void deleteValoration(@PathVariable("id") int id, @RequestHeader("Authorization") String token) {
        valorationService.deleteValoration(id, token);
    }
}
