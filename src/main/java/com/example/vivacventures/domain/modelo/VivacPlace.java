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
    private boolean isFavorite;

    public VivacPlace(int id, String name, String description, double latitude, double longitude, String username, int capacity, LocalDate date, List<Valoration> valorations, String type, double price, List<String> images) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.username = username;
        this.capacity = capacity;
        this.date = date;
        this.valorations = valorations;
        this.type = type;
        this.price = price;
        this.images = images;
    }
}
