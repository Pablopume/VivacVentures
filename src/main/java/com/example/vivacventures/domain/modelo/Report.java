package com.example.vivacventures.domain.modelo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    private int id;
    private String username;
    private int vivacPlaceId;
    private String description;
}
