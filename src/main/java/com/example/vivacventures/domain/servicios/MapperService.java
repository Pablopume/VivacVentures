package com.example.vivacventures.domain.servicios;

import com.example.vivacventures.data.modelo.ImageEntity;
import com.example.vivacventures.data.modelo.ValorationEntity;
import com.example.vivacventures.data.modelo.VivacPlaceEntity;
import com.example.vivacventures.data.modelo.mappers.ValorationEntityMapper;
import com.example.vivacventures.data.repository.VivacPlaceRepository;
import com.example.vivacventures.domain.modelo.Valoration;
import com.example.vivacventures.domain.modelo.VivacPlace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MapperService {
    private final ValorationEntityMapper valorationEntityMapper;

    public VivacPlace toVivacPlace(VivacPlaceEntity vivacPlace) {
        List<Valoration> valorations = vivacPlace.getValorations().stream().map(valorationEntityMapper::toValoration).toList();
        List<String> images = new ArrayList<>();
        vivacPlace.getImages().forEach(image -> images.add(image.getUrl()));
        return new VivacPlace(vivacPlace.getId(), vivacPlace.getName(), vivacPlace.getDescription(), vivacPlace.getLatitude(), vivacPlace.getLongitude(), vivacPlace.getUsername(), vivacPlace.getCapacity(), vivacPlace.getDate(), valorations, vivacPlace.getType(), vivacPlace.getPrice(), images);
    }

    public VivacPlaceEntity toVivacPlaceEntity(VivacPlace vivacPlace) {
        List<ImageEntity> images = new ArrayList<>();
        List<ValorationEntity> valorations = new ArrayList<>();
        vivacPlace.getValorations().forEach(valoration -> valorations.add(valorationEntityMapper.toValorationEntity(valoration)));
        VivacPlaceEntity vivacPlaceEntity = new VivacPlaceEntity(vivacPlace.getId(), vivacPlace.getName(), vivacPlace.getType(), vivacPlace.getLatitude(), vivacPlace.getLongitude(), vivacPlace.getUsername(), vivacPlace.getDescription(), vivacPlace.getDate(), vivacPlace.getCapacity(), valorations, vivacPlace.getPrice(), images);
        vivacPlace.getImages().forEach(image -> images.add(new ImageEntity(0, image, vivacPlaceEntity)));
        vivacPlaceEntity.setImages(images);
        return vivacPlaceEntity;

    }


}
