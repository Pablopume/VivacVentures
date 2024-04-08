package com.example.vivacventures.rest;

import com.example.vivacventures.domain.modelo.VivacPlace;
import com.example.vivacventures.domain.servicios.VivacPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class VivacPlaceRest {
    private final VivacPlaceService vivacPlaceService;
    @GetMapping("/vivacplaces")
    public List<VivacPlace> getVivacPlaces() {
        return vivacPlaceService.getVivacPlaces();
    }

    @GetMapping("/type")
    public List<VivacPlace> getVivacPlaceByType(@RequestParam("type") String type) {
        return vivacPlaceService.getVivacPlaceByType(type);
    }
    @GetMapping("/nearby")
    public List<VivacPlace> getVivacByLatitudeAndLongitude(@RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude) {
        return vivacPlaceService.getVivacByLatitudeAndLongitude(latitude, longitude);
    }
}
