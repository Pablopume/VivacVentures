package com.example.vivacventures.domain.modelo.dto;

import com.example.vivacventures.domain.modelo.VivacPlace;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
public class ListaDTO {
    private int id;
    private String name;
    private String username;
    private List<VivacPlace> vivacPlaces;
}
