package com.example.vivacventures.domain.servicios;

import com.example.vivacventures.common.Constantes;
import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.modelo.mappers.UserEntityMapper;
import com.example.vivacventures.data.repository.UserRepository;
import com.example.vivacventures.domain.common.Config;
import com.example.vivacventures.domain.common.DomainConstants;
import com.example.vivacventures.domain.modelo.User;
import com.example.vivacventures.domain.modelo.dto.UserRegisterDTO;
import com.example.vivacventures.util.JwtTokenUtil;
import io.jsonwebtoken.Jwts;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;

    private final Config config;
    private final JwtTokenUtil jwtTokenUtil;

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



    public boolean register(UserRegisterDTO userRegisterDTO) {
        UserEntity userEntity = userEntityMapper.toUserEntity(userRegisterDTO);
        UserEntity usuarioRepetido = userRepository.findByUsername(userRegisterDTO.getUsername());

        if (usuarioRepetido == null ){
            String randomString = UUID.randomUUID().toString();
            userEntity.setRol("USER");
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userEntity.setRandomStringVerified(randomString);
            userEntity.setVerificationExpirationDate(LocalDateTime.now());

            String url = "http://localhost:8764/auth/verified?verifiedString=" + userEntity.getRandomStringVerified();

            MandarMail mandarMail = new MandarMail();

            try {
                mandarMail.generateAndSendEmail(userEntity.getEmail(),
                        "<html><body><a href=\"" + url +  "\">Pulse para autenticar usuario</a></body></html>", "VivacVentures. Autenticación de Usuario");
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

            userRepository.save(userEntity);
            return true;

        } else {
            return false;
        }
    }
    private PrivateKey getPrivateKey() {
        try {
            KeyStore ks = KeyStore.getInstance(Constantes.PKCS_12);
            try (FileInputStream fis = new FileInputStream(path)) {
                ks.load(fis, password.toCharArray());
            }
            KeyStore.PasswordProtection pt = new KeyStore.PasswordProtection(password.toCharArray());
            KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(userkeystore, pt);
            if (pkEntry != null) {
                return pkEntry.getPrivateKey();
            } else {
//                throw new CertificateException(Constantes.NO_SE_ENCONTRO_LA_ENTRADA_DE_CLAVE_PRIVADA_EN_EL_KEYSTORE);
                throw new RuntimeException(Constantes.NO_SE_ENCONTRO_LA_ENTRADA_DE_CLAVE_PRIVADA_EN_EL_KEYSTORE);
            }
        } catch (Exception e) {
//            throw new CertificateException(Constantes.ERROR_AL_CARGAR_LA_CLAVE_PRIVADA_DEL_KEYSTORE);
            throw new RuntimeException(Constantes.ERROR_AL_CARGAR_LA_CLAVE_PRIVADA_DEL_KEYSTORE);
        }
    }


    public String autenticarse(String verifiedString) {
        String message = "";
        UserEntity user = userRepository.findByRandomStringVerified(verifiedString);
        String botonReenviar = "<a href=\"http://localhost:8764/auth/resendEmail?verifiedString=" + verifiedString + "\">Reenviar link</a>";



        if (user.getRandomStringVerified() != null) {
            if (LocalDateTime.now().isBefore(user.getVerificationExpirationDate().plusSeconds(accessExpiration))){
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
                    "<html><body><a href=\"" + url +  "\">Pulse para autenticar usuario</a></body></html>", "VivacVentures. Autenticación de Usuario");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return "Email reenviado. Revise su correo";
    }
    private String generateAccessToken(String subject, String rol) {
        java.util.Date now = new java.util.Date();
        return Jwts.builder()
                .setSubject(subject)
                .claim(Constantes.ROL, rol)
                .setIssuedAt(now)
                .setExpiration(java.util.Date.from(LocalDateTime.now().plusSeconds(accessExpiration)
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(getPrivateKey())
                .compact();
    }
}
