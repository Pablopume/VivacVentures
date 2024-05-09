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
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "vivac_place_id", referencedColumnName = "id")
    private VivacPlaceEntity vivacPlace;

    public FavoritoEntity(UserEntity user, VivacPlaceEntity vivacPlace) {
        this.user = user;
        this.vivacPlace = vivacPlace;
    }
}
