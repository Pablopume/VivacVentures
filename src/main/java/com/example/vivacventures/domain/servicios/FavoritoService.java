package com.example.vivacventures.domain.servicios;

import com.example.vivacventures.data.modelo.FavoritoEntity;
import com.example.vivacventures.data.modelo.ListaEntity;
import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.modelo.VivacPlaceEntity;
import com.example.vivacventures.data.modelo.mappers.VivacEntityMapper;
import com.example.vivacventures.data.repository.FavoritoRepository;
import com.example.vivacventures.data.repository.ListaRepository;
import com.example.vivacventures.data.repository.UserRepository;
import com.example.vivacventures.data.repository.VivacPlaceRepository;
import com.example.vivacventures.domain.common.DomainConstants;
import com.example.vivacventures.domain.modelo.FavoritesVivacPlaces;
import com.example.vivacventures.domain.modelo.VivacPlace;
import com.example.vivacventures.domain.modelo.exceptions.NoExisteException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoritoService {
    private final FavoritoRepository favoritoRepository;
    private final VivacPlaceRepository vivacPlaceRepository;
    private final UserRepository userRepository;
    private final MapperService mapperService;
    private final ListaRepository listaRepository;
    private final VivacEntityMapper vivacEntityMapper;


    public void saveFavorito(int listaId, int vivacId) {

        ListaEntity listaEntity = listaRepository.findById(listaId);
        VivacPlaceEntity vivacPlaceEntity = vivacPlaceRepository.getVivacPlaceEntitiesById(vivacId);
        if (listaEntity == null || vivacPlaceEntity == null)
            throw new NoExisteException(DomainConstants.USER_OR_VIVAC_PLACE_NOT_FOUND);
        else
            favoritoRepository.save(new FavoritoEntity(listaEntity, vivacPlaceEntity));
    }

    public void deleteFavorito(int id, int vivacId) {
        ListaEntity listaEntity = listaRepository.findById(id);
        VivacPlaceEntity vivacPlaceEntity = vivacPlaceRepository.getVivacPlaceEntitiesById(vivacId);
        if (listaEntity == null || vivacPlaceEntity == null)
            throw new NoExisteException(DomainConstants.USER_OR_VIVAC_PLACE_NOT_FOUND);
        else
            favoritoRepository.deleteByListaAndVivacPlace(listaEntity, vivacPlaceEntity);
    }

    public List<FavoritesVivacPlaces> getFavoritos(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null){
            throw new NoExisteException(DomainConstants.USER_NOT_FOUND);
        }else{
            return favoritoRepository.getFavouritesVivacPlaces(username).stream().map(mapperService::objectToFavoriteVivacPlace).toList();
        }
    }
}
