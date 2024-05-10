package com.example.vivacventures.domain.servicios;

import com.example.vivacventures.common.Constantes;
import com.example.vivacventures.data.modelo.ImageEntity;
import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.modelo.VivacPlaceEntity;
import com.example.vivacventures.data.modelo.mappers.VivacEntityMapper;
import com.example.vivacventures.data.repository.UserRepository;
import com.example.vivacventures.data.repository.VivacPlaceRepository;
import com.example.vivacventures.domain.modelo.VivacPlace;
import com.example.vivacventures.domain.modelo.exceptions.NotVerificatedException;
import com.example.vivacventures.security.KeyProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VivacPlaceService {
    private final VivacPlaceRepository vivacPlaceRepository;
    private final VivacEntityMapper vivacEntityMapper;
    private final MapperService mapperService;
    private final KeyProvider keyProvider;
    private final UserRepository userRepository;
    @Value(Constantes.APPLICATION_SECURITY_KEYSTORE_USERKEYSTORE)
    private String userkeystore;
    public List<VivacPlace> getVivacPlaceByType(String type) {
        List<VivacPlaceEntity> vivacPlaceEntities = vivacPlaceRepository.getVivacByType(type).stream().toList();
        return vivacPlaceEntities.stream().map(mapperService::toVivacPlace).toList();
    }

    public List<VivacPlace> getVivacPlaceByUsername(String username) {
        List<VivacPlaceEntity> vivacPlaceEntities = vivacPlaceRepository.getVivacByUser(username).stream().toList();
        return vivacPlaceEntities.stream().map(mapperService::toVivacPlace).toList();
    }

    public List<VivacPlace> getVivacByLatitudeAndLongitude(double userLatitude, double userLongitude) {
        List<VivacPlaceEntity> vivacPlaceEntities = vivacPlaceRepository.findNearbyPlaces(userLatitude, userLongitude);
        return vivacPlaceEntities.stream().map(mapperService::toVivacPlace).toList();
    }


    public List<VivacPlace> getVivacPlaces() {
        return  vivacPlaceRepository.findAllWithVivacPlaceEntity().stream().map(mapperService::toVivacPlace).toList();
    }

    public VivacPlace saveVivacPlace(VivacPlace vivacPlace) {
        VivacPlaceEntity vivacPlaceEntity = mapperService.toVivacPlaceEntity(vivacPlace);
        vivacPlaceRepository.save(vivacPlaceEntity);
        return vivacPlace;
    }
    public VivacPlace getVivacPlaceById(int id) {
        return mapperService.toVivacPlace(vivacPlaceRepository.getVivacPlaceEntitiesById(id));
    }
        @Transactional
        public void deleteVivacPlace(int id, String token) {
                String cleanedToken = token.replace("Bearer ", "");
                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(keyProvider.obtenerKeyPairUsuario(userkeystore).getPrivate())
                        .build().parseClaimsJws(cleanedToken);

                Integer userId = claimsJws.getBody().get("id", Integer.class);

                UserEntity userEntity = userRepository.findById(userId);

                VivacPlaceEntity vivacPlaceEntity = vivacPlaceRepository.getVivacPlaceEntitiesById(id);

                if (vivacPlaceEntity.getUsername().equals(userEntity.getUsername())) {
                    vivacPlaceRepository.deleteById(id);
                } else {
                    throw new NotVerificatedException("No tienes permisos para borrar este lugar");

                }
        }

        }





