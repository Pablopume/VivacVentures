package com.example.vivacventures.data.modelo.mappers;

import com.example.vivacventures.domain.modelo.VivacPlace;
import com.example.vivacventures.data.modelo.VivacPlaceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VivacEntityMapper {
    VivacPlace toVivacPlace(VivacPlaceEntity vivacPlaceEntity);
    VivacPlaceEntity toVivacPlaceEntity(VivacPlace vivacPlace);
}
