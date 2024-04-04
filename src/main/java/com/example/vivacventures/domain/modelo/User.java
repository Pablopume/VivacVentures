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
public class User {
private int id;
private String username;
private String password;
private String email;
private String rol;
private List<Valoration> valorations;

}
