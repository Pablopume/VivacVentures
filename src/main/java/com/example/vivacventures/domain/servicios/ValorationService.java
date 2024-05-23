package com.example.vivacventures.domain.servicios;

import com.example.vivacventures.common.Constantes;
import com.example.vivacventures.data.modelo.LoginToken;
import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.modelo.ValorationEntity;
import com.example.vivacventures.data.modelo.mappers.UserEntityMapper;
import com.example.vivacventures.data.modelo.mappers.ValorationEntityMapper;
import com.example.vivacventures.data.repository.UserRepository;
import com.example.vivacventures.data.repository.ValorationRepository;
import com.example.vivacventures.domain.modelo.User;
import com.example.vivacventures.domain.modelo.Valoration;
import com.example.vivacventures.domain.modelo.dto.UserRegisterDTO;
import com.example.vivacventures.domain.modelo.exceptions.*;
import com.example.vivacventures.security.KeyProvider;
import com.example.vivacventures.util.Utils;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ValorationService {
    private final ValorationRepository valorationRepository;
    private final UserRepository userRepository;
    private final MapperService mapperService;
    private final KeyProvider keyProvider;
    private final ValorationEntityMapper valorationEntityMapper;
    @Value(Constantes.APPLICATION_SECURITY_KEYSTORE_USERKEYSTORE)
    private String userkeystore;
    @Transactional
    public void deleteValoration(int id, String token) {

        String cleanedToken = token.replace("Bearer ", "");
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(keyProvider.obtenerKeyPairUsuario(userkeystore).getPrivate())
                .build().parseClaimsJws(cleanedToken);

        Integer userId = claimsJws.getBody().get("id", Integer.class);

        UserEntity userEntity = userRepository.findById(userId);
        ValorationEntity valorationEntity = valorationRepository.findById(id);

        if (valorationEntity.getUserEntity()==userEntity) {
            valorationRepository.deleteById(id);
        }
        else{
            throw new NotVerificatedException("Esa valoración no es tuya");
        }


    }

    public Valoration saveValoration(Valoration valoration){
        UserEntity userEntity = userRepository.findByUsername(valoration.getUsername());
        ValorationEntity valorationEntity1 = valorationRepository.findByUserEntity(userEntity.getId());
        if (valorationEntity1!=null){
            throw new AlreadyValorationException("Ya has valorado este lugar");
        }
        if (valoration.getScore()<=0 || valoration.getScore()>5){
            throw new BadScoreException("El score debe estar entre 1 y 5");
        }
        ValorationEntity valorationEntity = mapperService.toValorationEntity(valoration);
        valorationRepository.insertValoration(valorationEntity.getVivacPlaceEntity().getId(), valorationEntity.getUserEntity().getId(), valorationEntity.getScore(), valorationEntity.getReview(),valorationEntity.getDate());
        return valoration;
    }

}
