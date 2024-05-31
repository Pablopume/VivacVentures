package com.example.vivacventures.domain.modelo.dto;

import com.example.vivacventures.domain.modelo.FavoritesVivacPlaces;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListaDTO {
    private int id;
    private String name;
    private String username;
    private List<FavoritesVivacPlaces> vivacPlaces;

    public ListaDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ListaDTO(int id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }
}
