package com.example.vivacventures.data.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "lista")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @OneToMany(mappedBy = "lista", cascade = CascadeType.REMOVE)
    private List<FavoritoEntity> favoritos;

    @OneToMany(mappedBy = "lista", cascade = CascadeType.REMOVE)
    private List<ListaUserEntity> listaUsers;

    public ListaEntity(int id, String name, UserEntity user, List<FavoritoEntity> favoritos) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.favoritos = favoritos;
    }
}


