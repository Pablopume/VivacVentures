package com.example.vivacventures.web.domain.service;

import com.example.vivacventures.data.modelo.LoginToken;
import com.example.vivacventures.data.modelo.UserEntity;

public interface UsuarioService  {

//     UserEntity register(UserRegisterAdminDTO user);

     LoginToken login(String user, String password);
}
