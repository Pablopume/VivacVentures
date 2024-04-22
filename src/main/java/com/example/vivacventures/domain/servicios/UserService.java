package com.example.vivacventures.domain.servicios;

import com.example.vivacventures.common.Constantes;
import com.example.vivacventures.data.modelo.LoginToken;
import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.modelo.mappers.UserEntityMapper;
import com.example.vivacventures.data.repository.UserRepository;
import com.example.vivacventures.domain.common.Config;
import com.example.vivacventures.domain.common.DomainConstants;
import com.example.vivacventures.domain.modelo.User;
import com.example.vivacventures.domain.modelo.dto.UserRegisterDTO;
import com.example.vivacventures.domain.modelo.exceptions.Exception401;
import com.example.vivacventures.security.KeyProvider;
import com.example.vivacventures.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;
    private final KeyProvider keyProvider;
    private final Config config;
    private final JwtTokenUtil jwtTokenUtil;
    private final MandarMail mandarMail;
    private final AuthenticationManager authenticationManager;
    @Value(Constantes.APPLICATION_SECURITY_JWT_REFRESH_TOKEN_EXPIRATION)
    private int refreshExpiration;
    @Value(Constantes.APPLICATION_SECURITY_JWT_ACCESS_TOKEN_EXPIRATION)
    private int accessExpiration;
    @Value(Constantes.APPLICATION_SECURITY_KEYSTORE_PATH)
    private String path;
    @Value(Constantes.APPLICATION_SECURITY_KEYSTORE_PASSWORD)
    private String password;
    @Value(Constantes.APPLICATION_SECURITY_KEYSTORE_USERKEYSTORE)
    private String userkeystore;


    public LoginToken doLogin(String user, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (authentication.isAuthenticated()) {
            String accessToken = generateToken(user);
            String refreshToken = generateRefreshToken(user);
            return new LoginToken(accessToken, refreshToken);
        }
        //nunca va a llegar aquí ya que si no se autentica lanza la excepcion de hibernate, por eso devuelvo un null
        else {
            return null;
        }
    }


    public boolean register(UserRegisterDTO userRegisterDTO) {
        UserEntity userEntity = userEntityMapper.toUserEntity(userRegisterDTO);
        UserEntity usuarioRepetido = userRepository.findByUsername(userRegisterDTO.getUsername());

        if (usuarioRepetido == null) {
            String randomString = UUID.randomUUID().toString();
            userEntity.setRol("USER");
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userEntity.setRandomStringVerified(randomString);
            userEntity.setVerificationExpirationDate(LocalDateTime.now());

            String url = "http://localhost:8764/auth/verified?verifiedString=" + userEntity.getRandomStringVerified();


            try {
                mandarMail.generateAndSendEmail(userEntity.getEmail(),
                        "<html><body><a href=\"" + url + "\">Pulse para autenticar usuario</a></body></html>", "VivacVentures. Autenticación de Usuario");
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

            userRepository.save(userEntity);
            return true;

        } else {
            return false;
        }
    }


    public String autenticarse(String verifiedString) {
        String message = "";
        UserEntity user = userRepository.findByRandomStringVerified(verifiedString);
        String botonReenviar = "<a href=\"http://localhost:8764/auth/resendEmail?verifiedString=" + verifiedString + "\">Reenviar link</a>";


        if (user.getRandomStringVerified() != null) {
            if (LocalDateTime.now().isBefore(user.getVerificationExpirationDate().plusSeconds(accessExpiration))) {
                userRepository.updateUserVerified(verifiedString);
                message = "Usuario autenticado correctamente";
            } else {
                message = "Token de verificación caducado. " + botonReenviar;
            }
        } else {
            message = "Usuario no encontrado. " + botonReenviar;
        }
        return message;
    }

    public String resendEmail(String verifiedString) {
        UserEntity userEntity = userRepository.findByRandomStringVerified(verifiedString);
        String randomString = UUID.randomUUID().toString();
        userRepository.updateUserStringVerified(userEntity.getUsername(), randomString, LocalDateTime.now());

        String url = "http://localhost:8764/auth/verified?verifiedString=" + randomString;

        MandarMail mandarMail = new MandarMail();
        try {
            mandarMail.generateAndSendEmail(userEntity.getEmail(),
                    "<html><body><a href=\"" + url + "\">Pulse para autenticar usuario</a></body></html>", "VivacVentures. Autenticación de Usuario");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return "Email reenviado. Revise su correo";
    }

    public String generateToken(String nombre) {

        User credentials = userEntityMapper.toUser(userRepository.findByUsername(nombre));
        return Jwts.builder()
                .setSubject(credentials.getUsername())
                .claim("ROL", credentials.getRol())
                .setIssuedAt(new java.util.Date())
                .setExpiration(new Date(System.currentTimeMillis() + 300000))  // 300 seconds 300000
                .signWith(keyProvider.obtenerKeyPairUsuario(userkeystore).getPrivate())
                .compact();

    }

    public String generateRefreshToken(String nombre) {
        User credentials = userEntityMapper.toUser(userRepository.findByUsername(nombre));
        return Jwts.builder()
                .setSubject(credentials.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 600000000))
                .signWith(keyProvider.obtenerKeyPairUsuario(userkeystore).getPrivate())
                .compact();
    }


    public String refreshToken(String refreshToken) {
        if (validateToken(refreshToken)) {
            String username = Jwts.parserBuilder()
                    .setSigningKey(keyProvider.obtenerKeyPairUsuario(userkeystore).getPrivate())
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody()
                    .getSubject();
            return generateToken(username);
        }

        return null;
    }

    private boolean validateToken(String refreshtoken) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(keyProvider.obtenerKeyPairUsuario(userkeystore).getPublic())
                    .build()
                    .parseClaimsJws(refreshtoken);

            long expirationMillis = claimsJws.getBody().getExpiration().getTime();
            return System.currentTimeMillis() < expirationMillis;

        } catch (ExpiredJwtException e) {
            throw new Exception401("Token expirado");
        }
    }
}
