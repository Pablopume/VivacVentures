package com.example.vivacventures.data.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "valoration")
public class ValorationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "username",nullable = false)
    private String username;
    @Column(name = "place_id")
    private int placeId;
    @Column(name = "puntuation")
    private int puntuation;
    @Column(name = "review")
    private String review;
}
