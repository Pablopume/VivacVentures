package com.example.vivacventures.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.security.*;
import java.security.cert.X509Certificate;

@Log4j2
@Component
@RequiredArgsConstructor
public class KeyProvider {



    @Value("${application.security.keystore.password}")
    private String password;



    @Value("${application.security.keystore.path}")
    private String keystore;
    public KeyPair obtenerKeyPairUsuario(String nombreUsuario) {
        try {

            char[] keystorePassword = password.toCharArray();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            FileInputStream fis = new FileInputStream(keystore);
            ks.load(fis, keystorePassword);
            fis.close();

            char[] userPassword = password.toCharArray();
            Key userPrivateKey = ks.getKey(nombreUsuario, userPassword);
            X509Certificate userCertificate = (X509Certificate) ks.getCertificate(nombreUsuario);
            PublicKey userPublicKey = userCertificate.getPublicKey();
            return new KeyPair(userPublicKey, (PrivateKey) userPrivateKey);

        } catch (Exception ex) {
            log.error(ex.getMessage());
            return null;
        }
    }
}
