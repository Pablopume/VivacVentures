//package com.example.vivacventures.web.rest;
//
//import com.example.vivacventures.web.domain.model.UserRegisterAdminDTO;
//import com.example.vivacventures.web.domain.service.UsuarioService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/web/credentials")
//public class CredentialsController {
//    private UsuarioService usuarioService;
//
//    @ModelAttribute("user")
//    public UserRegisterAdminDTO userRegisterAdminDTO() {
//        return new UserRegisterAdminDTO();
//    }
//
//    @GetMapping
//    public String mostrarFormularioRegister() {
//        return "registro";
//    }
//
//    @PostMapping
//    public String registrarUsuario(@ModelAttribute("user") UserRegisterAdminDTO user) {
//        usuarioService.register(user);
//        return "redirect:/web/credentials?exito";
//    }
//}
