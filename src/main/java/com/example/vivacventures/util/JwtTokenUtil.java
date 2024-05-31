package com.example.vivacventures.util;

import com.example.vivacventures.common.Constantes;
import com.example.vivacventures.domain.common.Config;
import com.example.vivacventures.security.KeyProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private final KeyProvider keyProvider;

    @Value(ConstantesUtil.APPLICATION_PASSWORD)
    private String password;

    @Value(ConstantesUtil.APPLICATION_KEYSTORENAME)
    private String keystorename;

    @Value(Constantes.APPLICATION_SECURITY_KEYSTORE_USERKEYSTORE)
    private String userkeystore;

    public boolean validate(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(keyProvider.obtenerKeyPairUsuario(userkeystore).getPublic())
                .build()
                .parseClaimsJws(token);

        long expirationMillis = claimsJws.getBody().getExpiration().getTime();
        return System.currentTimeMillis() < expirationMillis;

    }

    public String getUsername(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(keyProvider.obtenerKeyPairUsuario(userkeystore).getPublic())
                .build()
                .parseClaimsJws(token);
        return claimsJws.getBody().getSubject();
    }

    public String getRole(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(keyProvider.obtenerKeyPairUsuario(userkeystore).getPublic())
                .build()
                .parseClaimsJws(token);
        return claimsJws.getBody().get(ConstantesUtil.ROL, String.class);
    }
}
