package com.example.vivacventures.ui.rest;

import com.example.vivacventures.domain.modelo.VivacPlace;
import com.example.vivacventures.domain.servicios.VivacPlaceService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class VivacPlaceRest {
    private final VivacPlaceService vivacPlaceService;
    @GetMapping("/vivacplaces")
    @Secured("ROLE_USER")
    public List<VivacPlace> getVivacPlaces() {
        return vivacPlaceService.getVivacPlaces();
    }

    @GetMapping("/{type}")
    @Secured("ROLE_USER")
    public List<VivacPlace> getVivacPlaceByType(@PathVariable("type") String type) {
        return vivacPlaceService.getVivacPlaceByType(type);
    }
    @GetMapping("/nearby")
    @Secured("ROLE_USER")
    public List<VivacPlace> getVivacByLatitudeAndLongitude(@RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude) {
        return vivacPlaceService.getVivacByLatitudeAndLongitude(latitude, longitude);
    }
    @PostMapping("/vivacplace")
    @Secured("ROLE_USER")
    public VivacPlace saveVivacPlace(@RequestBody VivacPlace vivacPlace) {
        return vivacPlaceService.saveVivacPlace(vivacPlace);
    }

    @GetMapping("/vivacplaces/{id}")
    @Secured("ROLE_USER")
    public VivacPlace getVivacPlacesById(@PathVariable int id) {
        return vivacPlaceService.getVivacPlaceById(id);
    }

    @DeleteMapping("/delete/{id}")
    @Secured("ROLE_USER")
    public void deleteVivacPlace(@PathVariable int id, @RequestHeader("Authorization") String token){
        vivacPlaceService.deleteVivacPlace(id, token);
    }
}
