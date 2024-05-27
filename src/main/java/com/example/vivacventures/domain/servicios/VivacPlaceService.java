package com.example.vivacventures.domain.servicios;

import com.example.vivacventures.common.Constantes;
import com.example.vivacventures.data.modelo.ImageEntity;
import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.modelo.VivacPlaceEntity;
import com.example.vivacventures.data.modelo.mappers.VivacEntityMapper;
import com.example.vivacventures.data.repository.ImageRepository;
import com.example.vivacventures.data.repository.UserRepository;
import com.example.vivacventures.data.repository.VivacPlaceRepository;
import com.example.vivacventures.domain.modelo.FavoritesVivacPlaces;
import com.example.vivacventures.domain.modelo.VivacPlace;
import com.example.vivacventures.domain.modelo.exceptions.BadPriceException;
import com.example.vivacventures.domain.modelo.exceptions.NoExisteException;
import com.example.vivacventures.domain.modelo.exceptions.NotVerificatedException;
import com.example.vivacventures.security.KeyProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VivacPlaceService {
    private final VivacPlaceRepository vivacPlaceRepository;
    private final ImageRepository imageRepository;
    private final VivacEntityMapper vivacEntityMapper;
    private final MapperService mapperService;
    private final KeyProvider keyProvider;
    private final UserRepository userRepository;
    @Value(Constantes.APPLICATION_SECURITY_KEYSTORE_USERKEYSTORE)
    private String userkeystore;



    public List<FavoritesVivacPlaces> getVivacPlaceByTypeAndUser(String type, String username) {
        return vivacPlaceRepository.getVivacByTypeAndUser(type, username).stream().map(mapperService::objectToFavoriteVivacPlace).toList();
    }

    public List<FavoritesVivacPlaces> getVivacPlaceByUsername(String username) {
        return vivacPlaceRepository.getVivacByUser(username).stream().map(mapperService::objectToFavoriteVivacPlace).toList();
    }


    public List<FavoritesVivacPlaces> getVivacByLatitudeAndLongitudeAndUser(double userLatitude, double userLongitude, String username) {
        return vivacPlaceRepository.findNearbyPlacesAndUser(userLatitude, userLongitude, username).stream().map(mapperService::objectToFavoriteVivacPlace).toList();
    }


    public List<VivacPlace> getVivacPlaces() {
        return vivacPlaceRepository.findAllWithVivacPlaceEntity().stream().map(mapperService::toVivacPlaceTipoAndNameAndDescriptionAndId).toList();
    }

    public List<FavoritesVivacPlaces> getVivacPlacesWithFavourites(String username) {
        return vivacPlaceRepository.getVivacPlaceWithFavourites(username).stream().map(mapperService::objectToFavoriteVivacPlace).toList();
    }

    public VivacPlace saveVivacPlace(VivacPlace vivacPlace) {
        if ( vivacPlace.getPrice() < 0) {
            throw new BadPriceException("El precio no puede ser negativo");
        }
        vivacPlace.setVisible(true);
        VivacPlaceEntity vivacPlaceEntity = mapperService.toVivacPlaceEntity(vivacPlace);
        vivacPlaceEntity.setVisible(true);
        vivacPlaceRepository.save(vivacPlaceEntity);
        return vivacPlace;
    }

    public boolean updateVivacPlace(VivacPlace vivacPlace) {
        VivacPlaceEntity vivacPlaceEntity = vivacPlaceRepository.getVivacPlaceEntitiesById(vivacPlace.getId());
        if (vivacPlaceEntity != null) {
            List<ImageEntity> images = imageRepository.getByVivacPlaceEntity(vivacPlaceEntity.getId());
            for (ImageEntity image : images) {
                imageRepository.delete(image);
            }
            List<String> imagesToSave = vivacPlace.getImages();
            for (String image : imagesToSave) {
                ImageEntity imageEntity = new ImageEntity();
                imageEntity.setUrl(image);
                imageEntity.setVivacPlaceEntity(vivacPlaceEntity);
                imageRepository.save(imageEntity);
            }
            vivacPlaceRepository.updateVivacPlaceEntitie(vivacPlace.getName(), vivacPlace.getType(), vivacPlace.getDescription(), vivacPlace.getLatitude(), vivacPlace.getLongitude(), vivacPlace.getUsername(), vivacPlace.getCapacity(), vivacPlace.getDate(), vivacPlace.getPrice(), vivacPlace.getId());
            return true;
        } else {
            throw new NoExisteException("No se ha podido actualizar el lugar");
        }
    }

    public VivacPlace getVivacPlaceById(int id) {
        return mapperService.toVivacPlace( vivacPlaceRepository.getVivacPlaceEntitiesById(id));
    }

    public VivacPlace getVivacPlaceByIdAndUsername(int id, String username) {
        List<Object[]> vivacPlaceDataList = vivacPlaceRepository.findVivacPlaceByIdAndUsername(id, username);
        Object[] vivacPlaceData = vivacPlaceDataList.isEmpty() ? null : vivacPlaceDataList.get(0);
        List<Object[]> valorationData = vivacPlaceRepository.findValorationsByVivacPlaceId(id);
        List<String> images = vivacPlaceRepository.findImagesByVivacPlaceId(id);

        return mapperService.mapToVivacPlace(vivacPlaceData, valorationData, images);
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





