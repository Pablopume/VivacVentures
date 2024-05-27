package com.example.vivacventures.domain.servicios;

import com.example.vivacventures.data.modelo.*;
import com.example.vivacventures.data.modelo.mappers.ValorationEntityMapper;
import com.example.vivacventures.data.repository.UserRepository;
import com.example.vivacventures.data.repository.VivacPlaceRepository;
import com.example.vivacventures.domain.modelo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MapperService {
    private final ValorationEntityMapper valorationEntityMapper;
    private final VivacPlaceRepository vivacPlaceRepository;
    private final UserRepository userRepository;

    public VivacPlace toVivacPlace(VivacPlaceEntity vivacPlace) {
        List<Valoration> valorations = vivacPlace.getValorations().stream().map(valorationEntityMapper::toValoration).toList();
        List<String> images = new ArrayList<>();
        vivacPlace.getImages().forEach(image -> images.add(image.getUrl()));
        return new VivacPlace(vivacPlace.getId(), vivacPlace.getName(), vivacPlace.getDescription(), vivacPlace.getLatitude(), vivacPlace.getLongitude(), vivacPlace.getUsername(), vivacPlace.getCapacity(), vivacPlace.getDate(), valorations, vivacPlace.getType(), vivacPlace.getPrice(), images,vivacPlace.isVisible());
    }

    public VivacPlace toVivacPlaceTipoAndNameAndDescriptionAndId(VivacPlaceEntity vivacPlace) {
        return new VivacPlace(vivacPlace.getId(), vivacPlace.getName(), vivacPlace.getDescription(), vivacPlace.getLatitude(), vivacPlace.getLongitude(), vivacPlace.getUsername(), vivacPlace.getCapacity(), vivacPlace.getDate(), new ArrayList<>(), vivacPlace.getType(), vivacPlace.getPrice(), new ArrayList<>(),vivacPlace.isVisible());
    }

    public ValorationEntity toValorationEntity(Valoration valoration) {
        VivacPlaceEntity vivacPlaceEntity = vivacPlaceRepository.getVivacPlaceEntitiesById(valoration.getVivacPlaceId());
        UserEntity userEntity = userRepository.findByUsername(valoration.getUsername());
        return new ValorationEntity(valoration.getId(), valoration.getScore(), valoration.getReview(), vivacPlaceEntity, userEntity,valoration.getDate());
    }

    public Valoration toValoration(ValorationEntity valorationEntity) {

        return new Valoration(valorationEntity.getId(), valorationEntity.getUserEntity().getUsername(), valorationEntity.getVivacPlaceEntity().getId(), valorationEntity.getScore(), valorationEntity.getReview(),valorationEntity.getDate());
    }

    public Report toReport(ReporteEntity reportEntity) {
        return new Report(reportEntity.getId(), reportEntity.getUsername(), reportEntity.getVivacPlaceEntity().getId(), reportEntity.getDescription());
    }


    public ListaEntity toListaEntity(Lista lista) {
        UserEntity userEntity = userRepository.findByUsername(lista.getUsername());
        return new ListaEntity(lista.getId(), lista.getName(), userEntity,lista.getFavoritos());
    }
    public ReporteEntity toReportEntity(Report report) {
        VivacPlaceEntity vivacPlaceEntity = vivacPlaceRepository.getVivacPlaceEntitiesById(report.getVivacPlaceId());
        return new ReporteEntity(report.getId(), report.getUsername(), vivacPlaceEntity, report.getDescription());
    }
    public VivacPlace mapToVivacPlace(Object[] vivacPlaceData, List<Object[]> valorationData, List<String> images) {
        VivacPlace vivacPlace = new VivacPlace();
        vivacPlace.setId((Integer) vivacPlaceData[0]);
        vivacPlace.setName((String) vivacPlaceData[1]);
        vivacPlace.setType((String) vivacPlaceData[2]);
        vivacPlace.setDescription((String) vivacPlaceData[3]);
        vivacPlace.setLatitude((Double) vivacPlaceData[4]);
        vivacPlace.setLongitude((Double) vivacPlaceData[5]);
        vivacPlace.setUsername((String) vivacPlaceData[6]);
        vivacPlace.setCapacity((Integer) vivacPlaceData[7]);
        vivacPlace.setDate(((java.sql.Date) vivacPlaceData[8]).toLocalDate());
        vivacPlace.setPrice((Double) vivacPlaceData[9]);
        Long favorite = (Long) vivacPlaceData[10];
        if (favorite == 1) {
            vivacPlace.setFavorite(true);
        } else {
            vivacPlace.setFavorite(false);
        }
        List<Valoration> valorations = valorationData.stream()
                .map(data -> {
                    Valoration valoration = new Valoration();
                    valoration.setId((Integer) data[0]);
                    valoration.setUsername((String) data[1]);
                    valoration.setVivacPlaceId(vivacPlace.getId());
                    valoration.setScore((Integer) data[2]);
                    valoration.setReview((String) data[3]);
                    valoration.setDate(((java.sql.Date) data[4]).toLocalDate());
                    return valoration;
                })
                .toList();
        vivacPlace.setValorations(valorations);

        vivacPlace.setImages(images);

        return vivacPlace;
    }

    public VivacPlaceEntity toVivacPlaceEntity(VivacPlace vivacPlace) {
        List<ImageEntity> images = new ArrayList<>();
        List<ValorationEntity> valorations = new ArrayList<>();
        vivacPlace.getValorations().forEach(valoration -> valorations.add(valorationEntityMapper.toValorationEntity(valoration)));
        VivacPlaceEntity vivacPlaceEntity = new VivacPlaceEntity(vivacPlace.getId(), vivacPlace.getName(), vivacPlace.getType(), vivacPlace.getLatitude(), vivacPlace.getLongitude(), vivacPlace.getUsername(), vivacPlace.getDescription(), vivacPlace.getDate(), vivacPlace.getCapacity(), valorations, vivacPlace.getPrice(), images, vivacPlace.isVisible());
        vivacPlace.getImages().forEach(image -> images.add(new ImageEntity(0, image, vivacPlaceEntity)));
        vivacPlaceEntity.setImages(images);
        return vivacPlaceEntity;

    }

    public AmigoEntity toAmigoEntity(Amigo amigo) {
        UserEntity requester = userRepository.findByUsername(amigo.getRequester());
        UserEntity requested = userRepository.findByUsername(amigo.getRequested());
        return new AmigoEntity(amigo.getId(), requester, requested, amigo.isStatus());
    }

    public Amigo toAmigo(AmigoEntity amigoEntity) {
        return new Amigo(amigoEntity.getId(), amigoEntity.getRequester().getUsername(), amigoEntity.getRequested().getUsername(), amigoEntity.isStatus());
    }

    public FavoritesVivacPlaces toFavoritesVivacPlaces(VivacPlaceEntity vivacPlaceEntity) {
        double valorations = 0;
        String image = null;
        if (!vivacPlaceEntity.getValorations().isEmpty()) {
            for (ValorationEntity valorationEntity : vivacPlaceEntity.getValorations()) {
                valorations += valorationEntity.getScore();
            }
            valorations = valorations / vivacPlaceEntity.getValorations().size();
        } else {
            valorations = -1;
        }

        if (!vivacPlaceEntity.getImages().isEmpty()) {
            image = vivacPlaceEntity.getImages().get(0).getUrl();
        }
        return new FavoritesVivacPlaces(vivacPlaceEntity.getId(), vivacPlaceEntity.getName(), vivacPlaceEntity.getType(), valorations, image, true);
    }

    public FavoritesVivacPlaces objectToFavoriteVivacPlace(Object[] object) {
        FavoritesVivacPlaces favoritesVivacPlaces = new FavoritesVivacPlaces();
        favoritesVivacPlaces.setId((Integer) object[0]);
        favoritesVivacPlaces.setName((String) object[1]);
        favoritesVivacPlaces.setType((String) object[2]);
        if (object[3] != null) {
            BigDecimal valorations = (BigDecimal) object[3];
            favoritesVivacPlaces.setValorations(valorations.doubleValue());
        } else {
            favoritesVivacPlaces.setValorations(-1);
        }
        if (object[4] != null) {
            favoritesVivacPlaces.setImages((String) object[4]);
        } else {
            favoritesVivacPlaces.setImages("");
        }
        favoritesVivacPlaces.setFavorite(object[5].equals(1));

        return favoritesVivacPlaces;
    }


}
