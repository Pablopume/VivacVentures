package com.example.vivacventures.data.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "username", unique = true)
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "rol")
    private String rol;
    @Column(name = "verified")
    private boolean verified;
    @Column(name = "verification_expiration_date")
    private LocalDateTime verificationExpirationDate;
    @Column(name = "random_string_verified", length = 255)
    private String randomStringVerified;
    @Column(name = "valorations")
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE)
    private List<ValorationEntity> valorations;
    @Column(name = "temporalPassword")
    private String temporalPassword;
    @OneToMany(mappedBy = "requester", cascade = CascadeType.REMOVE)
    private List<AmigoEntity> amigos;
    @OneToMany(mappedBy = "requested", cascade = CascadeType.REMOVE)
    private List<AmigoEntity> amigos2;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ListaEntity> listasCreadas;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ListaUserEntity> listasAcceso;

    public UserEntity(int id) {
        this.id = id;
    }
}
