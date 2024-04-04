package com.example.vivacventures.domain.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Valoration {
    private int id;
    private User user;
    private VivacPlace vivacPlace;
    private int score;
    private String review;
}
