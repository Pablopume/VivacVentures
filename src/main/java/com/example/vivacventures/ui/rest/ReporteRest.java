package com.example.vivacventures.ui.rest;

import com.example.vivacventures.domain.modelo.FavoritesVivacPlaces;
import com.example.vivacventures.domain.modelo.Report;
import com.example.vivacventures.domain.servicios.FavoritoService;
import com.example.vivacventures.domain.servicios.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ReporteRest {

    private final ReporteService reporteService;

    @PostMapping("/report")
    @Secured("ROLE_USER")
    public void saveReport(@RequestBody Report report) {
        reporteService.save(report);
    }

    @DeleteMapping("/report/delete/{id}")
    @Secured("ROLE_USER")
    public void deleteReport(@PathVariable("id") int id) {
        reporteService.delete(id);
    }


}
