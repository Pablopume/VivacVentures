package com.example.vivacventures.domain.servicios;

import com.example.vivacventures.data.modelo.*;
import com.example.vivacventures.data.repository.*;
import com.example.vivacventures.domain.modelo.FavoritesVivacPlaces;
import com.example.vivacventures.domain.modelo.Lista;
import com.example.vivacventures.domain.modelo.ListaUser;
import com.example.vivacventures.domain.modelo.VivacPlace;
import com.example.vivacventures.domain.modelo.dto.ListaDTO;
import com.example.vivacventures.domain.modelo.exceptions.NoExisteException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListaService {
    private final ListaRepository listaRepository;
    private final MapperService mapperService;
    private final UserRepository userRepository;
    private final ListaUserRepository listaUserRepository;
    private final FavoritoRepository favoritoRepository;
    private final VivacPlaceRepository vivacPlaceRepository;

    public void saveLista(Lista lista) {
        ListaEntity listaEntity = mapperService.toListaEntity(lista);
        lista.setFavoritos(new ArrayList<>());
        listaRepository.save(listaEntity);
    }

    @Transactional

    public void deleteLista(int id) {
        listaRepository.deleteById(id);
    }

    public void addFavoritoToLista(int id, int vivacId) {
        ListaEntity listaEntity = listaRepository.findById(id);
        VivacPlaceEntity vivacPlaceEntity = vivacPlaceRepository.findById(vivacId);
        FavoritoEntity favoritoEntity = new FavoritoEntity(listaEntity,vivacPlaceEntity);
        if (listaEntity == null)
            throw new NoExisteException("No existe la lista");
        else
            favoritoRepository.save(favoritoEntity);

    }

    public void deleteFavoritoFromList(int id, int vivacId) {
        ListaEntity listaEntity = listaRepository.findById(id);
        VivacPlaceEntity vivacPlaceEntity = vivacPlaceRepository.findById(vivacId);
        if (listaEntity == null)
            throw new NoExisteException("No existe la lista");
        else
            favoritoRepository.deleteByListaAndVivacPlace(listaEntity, vivacPlaceEntity);
    }


    public List<ListaDTO> getLists(String username) {
        UserEntity user = userRepository.findByUsername(username);
        List<ListaUserEntity> idListas = listaUserRepository.findByUser(user);
        List<ListaDTO> listas = new ArrayList<>();
        idListas.forEach(listaUserEntity -> {
            List<Object[]> listaEntities = listaRepository.findIdNameAndUsernameById(listaUserEntity.getLista().getId());
            Object[] listaEntity = listaEntities.isEmpty() ? null : listaEntities.get(0);
            ListaDTO lista = mapperService.toListaWithUserDTO(listaEntity);
            listas.add(lista);

        });

        List<FavoritesVivacPlaces> vivacPlaces = new ArrayList<>();
        listas.forEach(lista -> {
            lista.setVivacPlaces(vivacPlaces);
        });

        return listas;
    }

    public ListaDTO getList(int id) {
        List<Object[]> listaEntities = listaRepository.findIdNameAndUsernameById(id);
        Object[] listaEntity = listaEntities.isEmpty() ? null : listaEntities.get(0);
        ListaDTO lista = mapperService.toListaWithUserDTO(listaEntity);
        List<Integer>  vivacPlaceIds = listaRepository.findVivacPlaceIdsByListaId(lista.getId());
        List<FavoritesVivacPlaces> vivacPlaces = new ArrayList<>();
        vivacPlaceIds.forEach(vivacPlaceId -> {
            List<Object[]> vivacPlaceDataList = vivacPlaceRepository.getVivacPlaceWithFavouritesById(vivacPlaceId, lista.getUsername());
            Object[] vivacPlaceData = vivacPlaceDataList.isEmpty() ? null : vivacPlaceDataList.get(0);
            vivacPlaces.add(mapperService.objectToFavoriteVivacPlace(vivacPlaceData));
        });
        lista.setVivacPlaces(vivacPlaces);
        return lista;
    }


    public List<ListaDTO> getListsByUsernameAndVivacPlaceId(String username, int vivacPlaceId) {
        List<ListaEntity> listaEntities = listaRepository.findByUserAndVivacPlaceId(username, vivacPlaceId);
        List<ListaDTO> listas = new ArrayList<>();
        listaEntities.forEach(listaEntity -> listas.add(mapperService.toListaDTO(new Object[]{listaEntity.getId(), listaEntity.getName()})));
        listas.forEach(lista -> lista.setUsername(username));

        return listas;
    }


    public List<ListaDTO> getListsByUserId(int userId) {
        List<ListaEntity> listaEntities = listaRepository.getByUserId(userId);
        List<ListaDTO> listas = new ArrayList<>();
        listaEntities.forEach(listaEntity -> listas.add(mapperService.toListaDTO(new Object[]{listaEntity.getId(), listaEntity.getName()})));
        return listas;
    }

    public List<String> getWhoIsListShareWith(int listaId) {
        ListaEntity listaEntity = listaRepository.findById(listaId);
        List<ListaUserEntity> listaEntities = listaUserRepository.findByLista(listaEntity);
        List<String> usernames = new ArrayList<>();
        listaEntities.forEach(listaUserEntity -> usernames.add(listaUserEntity.getUser().getUsername()));

        return usernames;
    }

    public void shareList(int id, String username) {
        ListaEntity listaEntity = listaRepository.findById(id);
        UserEntity userEntity = userRepository.findByUsername(username);
        if (listaEntity == null || userEntity == null)
            throw new NoExisteException("No existe la lista o el usuario");
        else
            listaUserRepository.save(new ListaUserEntity(0, listaEntity, userEntity));
    }

    @Transactional
    public void deleteSharedList(int id, String username) {
        ListaEntity listaEntity = listaRepository.findById(id);
        UserEntity userEntity = userRepository.findByUsername(username);
        if (listaEntity == null || userEntity == null)
            throw new NoExisteException("No existe la lista o el usuario");
        else
            listaUserRepository.deleteByListaAndUser(listaEntity, userEntity);
    }


}
