package com.example.vivacventures.web.controlador;

import com.example.vivacventures.data.modelo.LoginToken;
import com.example.vivacventures.domain.modelo.UserWeb;
import com.example.vivacventures.domain.modelo.VivacPlace;
import com.example.vivacventures.domain.modelo.VivacPlaceWeb;
import com.example.vivacventures.domain.servicios.TokenService;
import com.example.vivacventures.domain.servicios.UserService;
import com.example.vivacventures.domain.servicios.VivacPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class RegistroControlador {

	private final TokenService tokenService;
	private final VivacPlaceService vivacPlaceService;
	private final UserService servicio;
	
	@GetMapping("/login")
	public String iniciarSesion() {
		return "login";
	}

	@GetMapping("/web/index")
	public String showIndex() {
		return "index"; // Esto corresponde al archivo `index.html` en `src/main/resources/templates`
	}

	@GetMapping("/web/login")
	public ResponseEntity< LoginToken> login(@RequestParam String username, @RequestParam String password) {
		LoginToken token = tokenService.doLogin(username, password);
		return ResponseEntity.ok(token);
	}

	@GetMapping("/users")
	public String users(Model model) {
		model.addAttribute("userWeb", new UserWeb());
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

	@ModelAttribute("vivacPlaceWeb")
	public VivacPlaceWeb vivacPlaceWeb() {
		return new VivacPlaceWeb();
	}

	@GetMapping("/vivacplacesweb")
	public String vivacplaces() {
		return "vivacplacesweb";
	}

	@GetMapping("/getvivacplaces")
	@Secured("ROLE_USER")
	public List<VivacPlace> getVivacPlaces() {
		return vivacPlaceService.getVivacPlaces();
	}
}