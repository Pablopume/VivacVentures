package com.example.vivacventures.domain.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoritesVivacPlaces {
    private int id;
    private String name;
    private String type;
//    private String description;
//    private List<Valoration> valorations;
    private double valorations;
    private String images;
    private boolean isFavorite;
}
