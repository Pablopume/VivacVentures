package com.example.vivacventures.domain.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Valoration {
    private int id;
    private String username;
    private int vivacPlaceId;
    private int score;
    private String review;
    private LocalDate date;
}
