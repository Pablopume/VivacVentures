package com.example.vivacventures.domain.servicios;

import com.example.vivacventures.data.modelo.ImageEntity;
import com.example.vivacventures.data.modelo.VivacPlaceEntity;
import com.example.vivacventures.data.modelo.mappers.VivacEntityMapper;
import com.example.vivacventures.data.repository.VivacPlaceRepository;
import com.example.vivacventures.domain.modelo.VivacPlace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VivacPlaceService {
    private final VivacPlaceRepository vivacPlaceRepository;
    private final VivacEntityMapper vivacEntityMapper;
    private final MapperService mapperService;

    public List<VivacPlace> getVivacPlaceByType(String type) {
        List<VivacPlaceEntity> vivacPlaceEntities = vivacPlaceRepository.getVivacByType(type).stream().toList();
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
}
