package com.example.vivacventures.ui.rest;

import com.example.vivacventures.domain.modelo.FavoritesVivacPlaces;
import com.example.vivacventures.domain.modelo.VivacPlace;
import com.example.vivacventures.domain.servicios.VivacPlaceService;
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

    @GetMapping("/vivacplaces/{username}")
    @Secured("ROLE_USER")
    public List<FavoritesVivacPlaces> getVivacPlacesWithFavourites(@PathVariable("username") String username) {
        return vivacPlaceService.getVivacPlacesWithFavourites(username);
    }

    @GetMapping("/vivacplaces/type/{type}/{username}")
    @Secured("ROLE_USER")
    public List<FavoritesVivacPlaces> getVivacPlaceByType(@PathVariable("type") String type, @PathVariable String username) {
        return vivacPlaceService.getVivacPlaceByTypeAndUser(type, username);
    }



    @GetMapping("/vivacplaces/user/{username}")
    @Secured("ROLE_USER")
    public List<FavoritesVivacPlaces> getVivacPlaceByUsername(@PathVariable("username") String username) {
        return vivacPlaceService.getVivacPlaceByUsername(username);
    }

    @GetMapping("/nearby/{username}")
    @Secured("ROLE_USER")
    public List<FavoritesVivacPlaces> getVivacByLatitudeAndLongitude(@RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude, @PathVariable String username) {
        return vivacPlaceService.getVivacByLatitudeAndLongitudeAndUser(latitude, longitude, username);
    }



    @PostMapping("/vivacplace")
    @Secured("ROLE_USER")
    public VivacPlace saveVivacPlace(@RequestBody VivacPlace vivacPlace) {
        return vivacPlaceService.saveVivacPlace(vivacPlace);
    }

    @PutMapping("/vivacplace")
    @Secured("ROLE_USER")
    public boolean updateVivacPlace(@RequestBody VivacPlace vivacPlace) {
        return vivacPlaceService.updateVivacPlace(vivacPlace);
    }

    @GetMapping("/vivacplaces/id/{id}")
    @Secured("ROLE_USER")
    public VivacPlace getVivacPlacesById(@PathVariable int id) {
        return vivacPlaceService.getVivacPlaceById(id);
    }

    @GetMapping("/vivacplaces/id/{id}/{username}")
    @Secured("ROLE_USER")
    public VivacPlace getVivacPlacesById(@PathVariable int id, @PathVariable String username) {
        return vivacPlaceService.getVivacPlaceByIdAndUsername(id, username);
    }

    @DeleteMapping("/delete/{id}")
    @Secured("ROLE_USER")
    public void deleteVivacPlace(@PathVariable int id, @RequestHeader("Authorization") String token) {
        vivacPlaceService.deleteVivacPlace(id, token);
    }
}
