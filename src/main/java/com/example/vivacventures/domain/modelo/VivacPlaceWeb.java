package com.example.vivacventures.domain.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VivacPlaceWeb {
    private int id;
    private String name;
    private String type;
    private String username;
//    private int capacity;
//    private double price;
    private boolean visible;
    private double mediaValorations;
    private List<String> images;

}
