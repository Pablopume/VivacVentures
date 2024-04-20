package com.example.vivacventures.util;

import com.example.vivacventures.common.Constantes;
import com.example.vivacventures.domain.common.Config;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PublicKey;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    private final Config config;
    public boolean validate(String token) throws ExpiredJwtException, SignatureException {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token);

        long expirationMillis = claimsJws.getBody().getExpiration().getTime();
        return System.currentTimeMillis() < expirationMillis;

    }

    public String getUsername(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token);
        return claimsJws.getBody().getSubject();
    }

    public String getRole(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token);
        return claimsJws.getBody().get(Constantes.ROL, String.class);
    }

    private PublicKey getPublicKey() {
        try {
            KeyStore ks = KeyStore.getInstance(Constantes.PKCS_12);
            try (FileInputStream fis = new FileInputStream(config.getProperty(Constantes.KEYSTORE))) {
                ks.load(fis, config.getProperty(Constantes.PASSWORD).toCharArray());
            }
            KeyStore.PasswordProtection pt = new KeyStore.PasswordProtection(config.getProperty(Constantes.PASSWORD).toCharArray());
            KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(Constantes.SERVER, pt);
            if (pkEntry != null) {
                return pkEntry.getCertificate().getPublicKey();
            } else {
//                throw new CertificateException(Constantes.NO_SE_ENCONTRO_LA_ENTRADA_DE_CLAVE_PRIVADA_EN_EL_KEYSTORE);
                throw new RuntimeException(Constantes.NO_SE_ENCONTRO_LA_ENTRADA_DE_CLAVE_PRIVADA_EN_EL_KEYSTORE);
            }
        } catch (Exception e) {
//            throw new CertificateException(Constantes.ERROR_AL_CARGAR_LA_CLAVE_PUBLICA_DEL_KEYSTORE +e);
            throw new RuntimeException(Constantes.ERROR_AL_CARGAR_LA_CLAVE_PUBLICA_DEL_KEYSTORE +e);
        }
    }
}
