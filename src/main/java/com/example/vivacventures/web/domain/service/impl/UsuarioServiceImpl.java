package com.example.vivacventures.web.domain.service.impl;

import com.example.vivacventures.common.Constantes;
import com.example.vivacventures.data.modelo.LoginToken;
import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.modelo.mappers.UserEntityMapper;
import com.example.vivacventures.data.repository.UserRepository;
import com.example.vivacventures.domain.modelo.User;
import com.example.vivacventures.domain.modelo.exceptions.Exception401;
import com.example.vivacventures.domain.modelo.exceptions.MailIncorrectoException;
import com.example.vivacventures.domain.modelo.exceptions.NotVerificatedException;
import com.example.vivacventures.domain.modelo.exceptions.YaExisteException;
import com.example.vivacventures.domain.servicios.MandarMail;
import com.example.vivacventures.domain.servicios.ServicioConstantes;
import com.example.vivacventures.security.KeyProvider;
import com.example.vivacventures.util.Utils;
import com.example.vivacventures.web.domain.service.UsuarioService;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;
    private final KeyProvider keyProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    @Value(Constantes.APPLICATION_SECURITY_JWT_REFRESH_TOKEN_EXPIRATION)
    private int refreshExpiration;
    @Value(Constantes.APPLICATION_SECURITY_JWT_ACCESS_TOKEN_EXPIRATION)
    private int accessExpiration;
    private final MandarMail mandarMail;

    @Value(Constantes.APPLICATION_SECURITY_KEYSTORE_USERKEYSTORE)
    private String userkeystore;

//    @Override
//    public UserEntity register(UserRegisterAdminDTO user) {
//        boolean admin = false;
//        if (user.getEmail() == null || !user.getEmail().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
//            throw new MailIncorrectoException("Email incorrecto");
//        }
//        UserEntity userEntity = userEntityMapper.toUserEntity(user);
//        UserEntity usuarioRepetido = userRepository.findByUsername(user.getUsername());
//        UserEntity emailRepetido = userRepository.findByEmail(user.getEmail());
//
//        if (usuarioRepetido == null ) {
//            if (emailRepetido == null) {
//                String randomString = Utils.randomBytes();
//                userEntity.setRol("USER");
//                userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
//                userEntity.setRandomStringVerified(randomString);
//                userEntity.setVerificationExpirationDate(LocalDateTime.now());
//                String url = ServicioConstantes.urlVerify + userEntity.getRandomStringVerified();
//
//                try {
//                    mandarMail.generateAndSendEmail(userEntity.getEmail(),
//                            "<html><body><a href=\"" + url + "\">Pulse para autenticar usuario</a></body></html>", "VivacVentures. Autenticación de Usuario");
//                } catch (MessagingException e) {
//                    throw new RuntimeException(e);
//                }
//                userRepository.save(userEntity);
//                return userEntity;
//            } else {
//                throw new YaExisteException("Email ya existente");
//            }
//        } else {
//            throw new YaExisteException("Usuario ya existente");
//        }
//    }

    @Override
    public LoginToken login(String user, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (authentication.isAuthenticated()) {
            String accessToken = generateToken(user);
            String refreshToken = generateRefreshToken(user);
            return new LoginToken(accessToken, refreshToken);
        }

        else {
            throw new NotVerificatedException("Usuario o contraseña incorrectos");
        }
    }

    public String generateToken(String nombre) {

        User credentials = userEntityMapper.toUser(userRepository.findByUsername(nombre));
        return Jwts.builder()
                .setSubject(credentials.getUsername())
                .claim("rol", credentials.getRol())
                .claim("id", credentials.getId())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(LocalDateTime.now().plusSeconds(accessExpiration)
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(keyProvider.obtenerKeyPairUsuario(userkeystore).getPrivate())
                .compact();

    }

    public String generateRefreshToken(String nombre) {
        User credentials = userEntityMapper.toUser(userRepository.findByUsername(nombre));
        return Jwts.builder()
                .setSubject(credentials.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(LocalDateTime.now().plusSeconds(refreshExpiration)
                        .atZone(ZoneId.systemDefault()).toInstant()))
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
