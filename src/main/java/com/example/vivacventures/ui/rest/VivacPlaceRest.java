package com.example.vivacventures.ui.rest;

import com.example.vivacventures.domain.modelo.FavoritesVivacPlaces;
import com.example.vivacventures.domain.modelo.VivacPlace;
import com.example.vivacventures.domain.modelo.VivacPlaceWeb;
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

    @GetMapping("/vivacplacesmy")
    @Secured("ROLE_USER")
    public List<FavoritesVivacPlaces> getVivacPlacesWithFavourites(@RequestHeader("Authorization") String token) {
        return vivacPlaceService.getVivacPlacesWithFavourites(token);
    }

    @GetMapping("/vivacplaces/type/{type}")
    @Secured("ROLE_USER")
    public List<FavoritesVivacPlaces> getVivacPlaceByType(@PathVariable("type") String type, @RequestHeader("Authorization") String token) {
        return vivacPlaceService.getVivacPlaceByTypeAndUser(type, token);
    }

    @GetMapping("/nearby")
    @Secured("ROLE_USER")
    public List<FavoritesVivacPlaces> getVivacByLatitudeAndLongitude(@RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude, @RequestHeader("Authorization") String token) {
        return vivacPlaceService.getVivacByLatitudeAndLongitudeAndUser(latitude, longitude, token);
    }

    @GetMapping("/vivacplaces/user/{username}")
    @Secured("ROLE_USER")
    public List<FavoritesVivacPlaces> getVivacPlaceByUsername(@PathVariable("username") String username) {
        return vivacPlaceService.getVivacPlaceByUsername(username);
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
    public VivacPlace getVivacPlacesById(@PathVariable int id, @RequestHeader("Authorization") String token) {
        return vivacPlaceService.getVivacPlaceByIdAndUsername(id, token);
    }

    @DeleteMapping("/delete/{id}")
    @Secured({"ROLE_USER" , "ROLE_ADMIN"})
    public void deleteVivacPlace(@PathVariable int id, @RequestHeader("Authorization") String token) {
        vivacPlaceService.deleteVivacPlace(id, token);
    }

    @GetMapping("/getvivacplacesweb")
    @Secured("ROLE_ADMIN")
    public List<VivacPlaceWeb> getVivacPlacesWeb() {
        return vivacPlaceService.getVivacPlacesWeb();
    }

    @PutMapping("/updateVisible/{id}")
    @Secured("ROLE_ADMIN")
    public void toggleVisibility(@PathVariable int id) {
        vivacPlaceService.updateVisibleVivacPlace(id);
    }

}
