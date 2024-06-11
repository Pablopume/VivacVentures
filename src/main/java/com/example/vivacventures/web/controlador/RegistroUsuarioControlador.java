package com.example.vivacventures.web.controlador;

import com.example.vivacventures.domain.modelo.UserWeb;
import com.example.vivacventures.domain.modelo.dto.AdminRegisterDTO;
import com.example.vivacventures.web.domain.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registro")
public class RegistroUsuarioControlador {


	private UsuarioService usuarioServicio;

	public RegistroUsuarioControlador(UsuarioService usuarioServicio) {
		super();
        this.usuarioServicio = usuarioServicio;
	}

	@ModelAttribute("user")
	public AdminRegisterDTO userRegisterAdminDTO() {
		return new AdminRegisterDTO();
	}

	@ModelAttribute("userWeb")
	public UserWeb userWeb() {
		return new UserWeb();
	}

	@GetMapping
	public String mostrarFormularioDeRegistro() {
		return "registro";
	}

//	@PostMapping
//	public String registrarCuentaDeUsuario(@ModelAttribute("user") UserRegisterAdminDTO user) {
//		usuarioServicio.register(user);
//		return "redirect:/registro?exito";
//	}






}