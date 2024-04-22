package com.example.vivacventures.domain.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VivacPlace {
    private int id;
    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private String username;
    private int capacity;
    private LocalDate date;
    private List<Valoration> valorations;
    private String type;
    private double price;
    private List<String> images;

}
