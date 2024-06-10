package com.example.vivacventures.domain.servicios;

import com.example.vivacventures.common.Constantes;
import com.example.vivacventures.data.modelo.LoginToken;
import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.modelo.mappers.UserEntityMapper;
import com.example.vivacventures.data.repository.UserRepository;
import com.example.vivacventures.domain.modelo.User;
import com.example.vivacventures.domain.modelo.exceptions.Exception401;
import com.example.vivacventures.domain.modelo.exceptions.NotVerificatedException;
import com.example.vivacventures.security.KeyProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
@Service
@RequiredArgsConstructor
public class TokenService {
    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;
    private final KeyProvider keyProvider;
    private final AuthenticationManager authenticationManager;
    @Value(Constantes.APPLICATION_SECURITY_JWT_REFRESH_TOKEN_EXPIRATION)
    private int refreshExpiration;
    @Value(Constantes.APPLICATION_SECURITY_JWT_ACCESS_TOKEN_EXPIRATION)
    private int accessExpiration;

    @Value(Constantes.APPLICATION_SECURITY_KEYSTORE_USERKEYSTORE)
    private String userkeystore;
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

    public LoginToken doLogin(String user, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (authentication.isAuthenticated()) {
            UserEntity userEntity = userRepository.findByUsername(user);
            if (!userEntity.isVerified()) {
                throw new NotVerificatedException("Usuario no verificado, diríjase a su correo para verificarlo");
            }
            String accessToken = generateToken(user);
            String refreshToken = generateRefreshToken(user);
            return new LoginToken(accessToken, refreshToken);
        } else {
            throw new BadCredentialsException("Usuario o contraseña incorrectos");
        }
    }
}
