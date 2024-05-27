package com.example.vivacventures.domain.servicios;

import com.example.vivacventures.data.modelo.FavoritoEntity;
import com.example.vivacventures.data.modelo.ListaEntity;
import com.example.vivacventures.data.modelo.ListaUserEntity;
import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.repository.FavoritoRepository;
import com.example.vivacventures.data.repository.ListaRepository;
import com.example.vivacventures.data.repository.ListaUserRepository;
import com.example.vivacventures.data.repository.UserRepository;
import com.example.vivacventures.domain.modelo.Lista;
import com.example.vivacventures.domain.modelo.exceptions.NoExisteException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListaService {
    private final ListaRepository listaRepository;
    private final MapperService mapperService;
    private final UserRepository userRepository;
    private final ListaUserRepository listaUserRepository;
    private final FavoritoRepository favoritoRepository;

    public void saveLista(Lista lista) {
        ListaEntity listaEntity = mapperService.toListaEntity(lista);
        listaRepository.save(listaEntity);
    }

    public void deleteLista(int id) {
        listaRepository.deleteById(id);
    }

    public void addFavoritoToLista(int id, int favoritoId) {
        ListaEntity listaEntity = listaRepository.findById(id);
        FavoritoEntity favoritoEntity = favoritoRepository.findById(favoritoId);
        if (listaEntity == null)
            throw new NoExisteException("No existe la lista");
        else
            listaEntity.getFavoritos().add(favoritoEntity);
        favoritoRepository.save(favoritoEntity);

    }

    public void shareList(int id, String username) {
        ListaEntity listaEntity = listaRepository.findById(id);
        UserEntity userEntity = userRepository.findByUsername(username);
        if (listaEntity == null || userEntity == null)
            throw new NoExisteException("No existe la lista o el usuario");
        else
            listaUserRepository.save(new ListaUserEntity(0, listaEntity, userEntity));
    }

    public void deleteSharedList(int id, String username) {
        ListaEntity listaEntity = listaRepository.findById(id);
        UserEntity userEntity = userRepository.findByUsername(username);
        if (listaEntity == null || userEntity == null)
            throw new NoExisteException("No existe la lista o el usuario");
        else
            listaUserRepository.deleteByListaAndUser(listaEntity, userEntity);
    }


}
