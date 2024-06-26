package com.example.vivacventures.data.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "favorito")
public class FavoritoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "lista_id", referencedColumnName = "id")
    private ListaEntity lista;

    @ManyToOne
    @JoinColumn(name = "vivac_place_id", referencedColumnName = "id")
    private VivacPlaceEntity vivacPlace;

    public FavoritoEntity(ListaEntity lista, VivacPlaceEntity vivacPlace) {
        this.lista = lista;
        this.vivacPlace = vivacPlace;
    }
}
