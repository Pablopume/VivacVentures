package com.example.vivacventures.data.modelo.mappers;

import com.example.vivacventures.domain.modelo.Valoration;
import com.example.vivacventures.data.modelo.ValorationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ValorationEntityMapper {
    ValorationEntity toValorationEntity(Valoration valoration);

    Valoration toValoration(ValorationEntity valorationEntity);

}
