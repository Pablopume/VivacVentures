package com.example.vivacventures.domain.servicios;

import com.example.vivacventures.common.Constantes;
import com.example.vivacventures.data.modelo.ImageEntity;
import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.modelo.ValorationEntity;
import com.example.vivacventures.data.modelo.VivacPlaceEntity;
import com.example.vivacventures.data.modelo.mappers.VivacEntityMapper;
import com.example.vivacventures.data.repository.ImageRepository;
import com.example.vivacventures.data.repository.UserRepository;
import com.example.vivacventures.data.repository.ValorationRepository;
import com.example.vivacventures.data.repository.VivacPlaceRepository;
import com.example.vivacventures.domain.modelo.FavoritesVivacPlaces;
import com.example.vivacventures.domain.modelo.VivacPlace;
import com.example.vivacventures.domain.modelo.VivacPlaceWeb;
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
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class VivacPlaceService {
    private final VivacPlaceRepository vivacPlaceRepository;
    private final ImageRepository imageRepository;
    private final ValorationRepository valorationRepository;
    private final VivacEntityMapper vivacEntityMapper;
    private final MapperService mapperService;
    private final KeyProvider keyProvider;
    private final UserRepository userRepository;
    @Value(Constantes.APPLICATION_SECURITY_KEYSTORE_USERKEYSTORE)
    private String userkeystore;


    public List<FavoritesVivacPlaces> getVivacPlaceByTypeAndUser(String type, String token) {
        String username = usernameFromToken(token);
        return vivacPlaceRepository.getVivacByTypeAndUser(type, username).stream().map(mapperService::objectToFavoriteVivacPlace).toList();
    }

    public List<FavoritesVivacPlaces> getVivacPlaceByUsername(String username) {
        return vivacPlaceRepository.getVivacByUser(username).stream().map(mapperService::objectToFavoriteVivacPlace).toList();
    }


    public List<FavoritesVivacPlaces> getVivacByLatitudeAndLongitudeAndUser(double userLatitude, double userLongitude, String token) {
        String username = usernameFromToken(token);
        return vivacPlaceRepository.findNearbyPlacesAndUser(userLatitude, userLongitude, username).stream().map(mapperService::objectToFavoriteVivacPlace).toList();
    }


    public List<VivacPlace> getVivacPlaces() {
        return vivacPlaceRepository.findAllWithVivacPlaceEntity().stream().map(mapperService::toVivacPlaceTipoAndNameAndDescriptionAndId).toList();
    }

    public List<FavoritesVivacPlaces> getVivacPlacesWithFavourites(String token) {
        String username = usernameFromToken(token);
        return vivacPlaceRepository.getVivacPlaceWithFavourites(username).stream().map(mapperService::objectToFavoriteVivacPlace).toList();
    }

    public String usernameFromToken(String token) {
        String cleanedToken = token.replace("Bearer ", "");
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(keyProvider.obtenerKeyPairUsuario(userkeystore).getPrivate())
                .build().parseClaimsJws(cleanedToken);

        Integer userId = claimsJws.getBody().get("id", Integer.class);

        UserEntity userEntity = userRepository.findById(userId);

        return userEntity.getUsername();
    }

    public String rolFromToken(String token) {
        String cleanedToken = token.replace("Bearer ", "");
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(keyProvider.obtenerKeyPairUsuario(userkeystore).getPrivate())
                .build().parseClaimsJws(cleanedToken);

        return claimsJws.getBody().get("rol", String.class);
    }


    public VivacPlace saveVivacPlace(VivacPlace vivacPlace) {
        if (vivacPlace.getPrice() < 0) {
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
            List<ImageEntity> existingImages = imageRepository.getByVivacPlaceEntity(vivacPlaceEntity.getId());
            List<String> existingImageUrls = existingImages.stream().map(ImageEntity::getUrl).collect(toList());
            List<String> imagesToSave = vivacPlace.getImages();

            boolean imagesAreEqual = existingImageUrls.containsAll(imagesToSave) && imagesToSave.containsAll(existingImageUrls);

            if (!imagesAreEqual) {
                imageRepository.deleteAll(existingImages);

                for (String image : imagesToSave) {
                    ImageEntity imageEntity = new ImageEntity();
                    imageEntity.setUrl(image);
                    imageEntity.setVivacPlaceEntity(vivacPlaceEntity);
                    imageRepository.save(imageEntity);
                }
            }

            vivacPlaceRepository.updateVivacPlaceEntitie(
                    vivacPlace.getName(),
                    vivacPlace.getType(),
                    vivacPlace.getDescription(),
                    vivacPlace.getLatitude(),
                    vivacPlace.getLongitude(),
                    vivacPlace.getUsername(),
                    vivacPlace.getCapacity(),
                    vivacPlace.getDate(),
                    vivacPlace.getPrice(),
                    vivacPlace.getId()
            );
            return true;
        } else {
            throw new NoExisteException("No se ha podido actualizar el lugar");
        }
    }

    public VivacPlace getVivacPlaceById(int id) {
        return mapperService.toVivacPlace(vivacPlaceRepository.getVivacPlaceEntitiesById(id));
    }

    public VivacPlace getVivacPlaceByIdAndUsername(int id, String token) {
        String username = usernameFromToken(token);
        List<Object[]> vivacPlaceDataList = vivacPlaceRepository.findVivacPlaceByIdAndUsername(id, username);
        Object[] vivacPlaceData = vivacPlaceDataList.isEmpty() ? null : vivacPlaceDataList.get(0);
        List<Object[]> valorationData = vivacPlaceRepository.findValorationsByVivacPlaceId(id);
        List<String> images = vivacPlaceRepository.findImagesByVivacPlaceId(id);

        assert vivacPlaceData != null;
        return mapperService.mapToVivacPlace(vivacPlaceData, valorationData, images);
    }

    @Transactional
    public void deleteVivacPlace(int id, String token) {
        String username = usernameFromToken(token);
        String rol = rolFromToken(token);

        VivacPlaceEntity vivacPlaceEntity = vivacPlaceRepository.getVivacPlaceEntitiesById(id);

        if (vivacPlaceEntity.getUsername().equals(username) || rol.equals("ADMIN")) {
            vivacPlaceRepository.deleteById(id);
        } else {
            throw new NotVerificatedException("No tienes permisos para borrar este lugar");

        }
    }


    public List<VivacPlaceWeb> getVivacPlacesWeb() {
        return vivacPlaceRepository.findAllWithVivacPlaceEntityWeb().stream().map(vivacPlaceEntity -> {
                    VivacPlaceWeb vivacPlaceWeb = mapperService.toVivacPlaceWeb(vivacPlaceEntity);

                    List<ValorationEntity> valorations = valorationRepository.findByVivacPlaceEntity(vivacPlaceEntity);
                    if (!valorations.isEmpty()) {
                        double mediaValorations = valorations.stream()
                                .mapToDouble(ValorationEntity::getScore)
                                .average()
                                .orElse(0);
                        vivacPlaceWeb.setMediaValorations(mediaValorations);
                    }

                    List<ImageEntity> images = imageRepository.getByVivacPlaceEntity(vivacPlaceEntity.getId());
                    if (!images.isEmpty()) {
                        List<String> imageUrls = images.stream()
                                .map(ImageEntity::getUrl)
                                .collect(toList());
                        vivacPlaceWeb.setImages(imageUrls);
                    }

                    return vivacPlaceWeb;
                }
        ).collect(toList());

    }

    public void updateVisibleVivacPlace(int id) {
        VivacPlaceEntity vivacPlaceEntity = vivacPlaceRepository.getVivacPlaceEntitiesById(id);

        if (vivacPlaceEntity == null) {
            throw new NoExisteException("No existe el lugar");
        }

        boolean visible = !vivacPlaceEntity.isVisible();
        vivacPlaceRepository.updateByVisible(id, visible);
    }



}





