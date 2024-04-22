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
@Table(name = "valoration")
public class ValorationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "score")
    private int score;
    @Column(name = "review")
    private String review;
    @ManyToOne
    @JoinColumn(name = "vivac_id", insertable = false, updatable = false)
    private VivacPlaceEntity vivacPlaceEntity;
    @ManyToOne
    @JoinColumn(name = "username", insertable = false, updatable = false)
    private UserEntity userEntity;
}
