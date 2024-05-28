package com.example.vivacventures.domain.servicios;

import com.example.vivacventures.data.modelo.*;
import com.example.vivacventures.data.repository.*;
import com.example.vivacventures.domain.modelo.Lista;
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


    public List<ListaDTO> getLists(String username) {

        UserEntity userEntity = userRepository.findByUsername(username);
        List<ListaEntity> listaEntities = listaRepository.findAllByUser(userEntity);
        List<ListaDTO> listas = new ArrayList<>();
        listaEntities.forEach(listaEntity -> listas.add(mapperService.toListaDTO(listaEntity)));
        return listas;
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
