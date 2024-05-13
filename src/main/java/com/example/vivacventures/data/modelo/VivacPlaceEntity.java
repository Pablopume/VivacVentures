package com.example.vivacventures.data.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vivac_place")
public class VivacPlaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private String type;
    @Column(name = "latitude")
    private double latitude;
    @Column(name = "longitude")
    private double longitude;
    @Column(name = "username")
    private String username;
    @Column(name = "description")
    private String description;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "capacity")
    private int capacity;
    @OneToMany(mappedBy = "vivacPlaceEntity")
    private List<ValorationEntity> valorations;
    @Column(name = "price")
    private Double price;
    @Column(name = "image" )
    @OneToMany(mappedBy = "vivacPlaceEntity",cascade = CascadeType.PERSIST)
    private List<ImageEntity> images;
}
