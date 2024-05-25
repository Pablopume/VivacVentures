package com.example.vivacventures.data.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "amigo")
public class AmigoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity requester;
    @ManyToOne
    @JoinColumn(name = "user_id2", referencedColumnName = "id")
    private UserEntity requested;
    @Column(name = "status")
    private boolean status;
}
