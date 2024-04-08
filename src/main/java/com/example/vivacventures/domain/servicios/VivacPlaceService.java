package com.example.vivacventures.domain.servicios;

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

    public List<VivacPlace> getVivacPlaceByType(String type) {
        List<VivacPlaceEntity> vivacPlaceEntities = vivacPlaceRepository.getVivacByType(type);
        return vivacPlaceEntities.stream().map(vivacEntityMapper::toVivacPlace).toList();
    }



    public List<VivacPlace> getVivacPlaces() {
        return vivacPlaceRepository.findAllWithVivacPlaceEntity().stream().map(vivacEntityMapper::toVivacPlace).toList();
    }
}
