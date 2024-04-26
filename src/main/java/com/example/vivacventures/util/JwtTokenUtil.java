package com.example.vivacventures.util;

import com.example.vivacventures.common.Constantes;
import com.example.vivacventures.domain.common.Config;
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

    @Value(ConstantesUtil.APPLICATION_PASSWORD)
    private String password;

    @Value(ConstantesUtil.APPLICATION_KEYSTORENAME)
    private String keystorename;

    @Value(ConstantesUtil.APPLICATION_KEYSTORE)
    private String keystore;

    public boolean validate(String token) {
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


    private PublicKey getPublicKey() {
        try {

            char[] keystorePassword = password.toCharArray();
            KeyStore ks = KeyStore.getInstance(ConstantesUtil.PKCS_12);
            FileInputStream fis = new FileInputStream(keystore);
            ks.load(fis, keystorePassword);
            fis.close();
            X509Certificate userCertificate = (X509Certificate) ks.getCertificate(keystorename);
            return userCertificate.getPublicKey();


        } catch (Exception ex) {

            return null;
        }
    }


    public String getRole(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token);
        return claimsJws.getBody().get(ConstantesUtil.ROL, String.class);
    }
}
