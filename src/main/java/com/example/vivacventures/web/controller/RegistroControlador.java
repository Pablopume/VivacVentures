package com.example.vivacventures.web.controller;

import com.example.vivacventures.domain.modelo.VivacPlace;
import com.example.vivacventures.domain.modelo.dto.AdminRegisterDTO;
import com.example.vivacventures.domain.servicios.VivacPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/web")
public class RegistroControlador {
	private final VivacPlaceService vivacPlaceService;
	
	@GetMapping("/login")
	public String iniciarSesion() {
		return "login";
	}

	@GetMapping("/users")
	public String users() {
		return "users";
	}

	@GetMapping("/menu")
	public String menu() {
		return "menu";
	}

	@GetMapping("/error403")
	public String error403() {
		return "error403";
	}

	@ModelAttribute("user")
	public AdminRegisterDTO userRegisterAdminDTO() {
		return new AdminRegisterDTO();
	}
//
//	@ModelAttribute("userWeb")
//	public UserWeb userWeb() {
//		return new UserWeb();
//	}
//	@ModelAttribute("vivacPlaceWeb")
//	public VivacPlaceWeb vivacPlaceWeb() {
//		return new VivacPlaceWeb();
//	}

	@GetMapping("/vivacplacesweb")
	public String vivacplaces() {
		return "vivacplacesweb";
	}

	@GetMapping("/getvivacplaces")
	@Secured("ROLE_USER")
	public List<VivacPlace> getVivacPlaces() {
		return vivacPlaceService.getVivacPlaces();
	}

	@GetMapping("/registro")
	public String mostrarFormularioDeRegistro() {
		return "registro";
	}

}