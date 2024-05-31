package com.example.vivacventures.domain.modelo;

import com.example.vivacventures.data.modelo.FavoritoEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Lista {
    private int id;
    private String name;

    private String username;

    private List<FavoritoEntity> favoritos;
}
