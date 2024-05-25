package com.example.vivacventures.domain.servicios;
import com.example.vivacventures.common.Constantes;
import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.modelo.mappers.UserEntityMapper;
import com.example.vivacventures.data.repository.UserRepository;
import com.example.vivacventures.domain.modelo.dto.UserAmigoDTO;
import com.example.vivacventures.domain.modelo.dto.UserRegisterDTO;
import com.example.vivacventures.domain.modelo.exceptions.*;
import com.example.vivacventures.util.Utils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;
    private final MandarMail mandarMail;
    @Value(Constantes.APPLICATION_SECURITY_JWT_ACCESS_TOKEN_EXPIRATION)
    private int accessExpiration;

    public UserAmigoDTO getUserAmigo(String username) {
        return userRepository.getAllUsersWithVivacPlaceCount(username).stream()
                .map(user -> new UserAmigoDTO((String) user[0], ((Long) user[1]).intValue()))
                .findFirst()
                .orElseThrow(() -> new NoExisteException("Usuario no encontrado"));
    }



    public void changePassword(String email, String newPassword, String temporalPaswword) {
        UserEntity user = userRepository.findByEmail(email);
        if (!passwordEncoder.matches(temporalPaswword, user.getTemporalPassword())) {
            throw new BadPasswordException("Contraseña temporal incorrecta");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setTemporalPassword(null);
        userRepository.save(user);
    }

    public void forgotPassword(String email) {
        String randomString = Utils.randomBytes();
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NoExisteException("Email no encontrado");
        }
        try {
            mandarMail.generateAndSendEmail(email, "Su nueva contraseña temporal es: " + randomString, "VivacVentures. Nueva contraseña temporal");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
            user.setTemporalPassword(passwordEncoder.encode(randomString));
            userRepository.save(user);
    }

    public boolean register(UserRegisterDTO userRegisterDTO) {
        if (userRegisterDTO.getEmail() == null || !userRegisterDTO.getEmail().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            throw new MailIncorrectoException("Email incorrecto");
        }
        UserEntity userEntity = userEntityMapper.toUserEntity(userRegisterDTO);
        UserEntity usuarioRepetido = userRepository.findByUsername(userRegisterDTO.getUsername());
        UserEntity emailRepetido = userRepository.findByEmail(userRegisterDTO.getEmail());

        if (usuarioRepetido == null ) {
            if (emailRepetido == null) {
                String randomString = Utils.randomBytes();
                userEntity.setRol("USER");
                userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
                userEntity.setRandomStringVerified(randomString);
                userEntity.setVerificationExpirationDate(LocalDateTime.now());
                String url = ServicioConstantes.urlVerify + userEntity.getRandomStringVerified();

                try {
                    mandarMail.generateAndSendEmail(userEntity.getEmail(),
                            "<html><body><a href=\"" + url + "\">Pulse para autenticar usuario</a></body></html>", "VivacVentures. Autenticación de Usuario");
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
                userRepository.save(userEntity);
                return true;
            } else {
                throw new YaExisteException("Email ya existente");
            }
        } else {
            throw new YaExisteException("Usuario ya existente");
        }
    }


    public String autenticarse(String verifiedString) {
        String message = "";
        UserEntity user = userRepository.findByRandomStringVerified(verifiedString);
        String botonReenviar = ServicioConstantes.resend + verifiedString + "\">Reenviar link</a>";

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

        String url = ServicioConstantes.urlVerified + randomString;


        try {
            mandarMail.generateAndSendEmail(userEntity.getEmail(),
                    "<html><body><a href=\"" + url + "\">Pulse para autenticar usuario</a></body></html>", "VivacVentures. Autenticación de Usuario");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return "Email reenviado. Revise su correo";
    }


}
