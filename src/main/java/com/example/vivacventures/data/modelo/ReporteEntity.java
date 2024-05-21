package com.example.vivacventures.data.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "report")
public class ReporteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "username")
    private String username;

    @ManyToOne
    @JoinColumn(name = "vivac_place_id", referencedColumnName = "id")
    private VivacPlaceEntity vivacPlaceEntity;
    @Column(name = "description")
    private String description;



}
