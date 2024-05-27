package com.example.vivacventures.domain.servicios;

import com.example.vivacventures.data.modelo.AmigoEntity;
import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.repository.AmigoRepository;
import com.example.vivacventures.data.repository.UserRepository;
import com.example.vivacventures.domain.modelo.Amigo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AmigoService {
    private final AmigoRepository amigoRepository;
    private final MapperService mapperService;
    private final UserRepository userRepository;

    public void mandarPeticionAmistad(Amigo amigo) {
        amigo.setStatus(false);
        AmigoEntity amigoEntity = mapperService.toAmigoEntity(amigo);
//        if (amigoRepository.existsByRequesterAndRequested(mapperService.toUserEntity(amigoEntity.getRequester()), mapperService.toUserEntity(amigo.getRequested()))) {
  //          return;
    //    }


        amigoRepository.save(amigoEntity);
    }

    public void aceptarPeticionAmistad(Amigo amigo) {
        AmigoEntity amigoEntity = amigoRepository.findById(amigo.getId());
        amigoEntity.setStatus(true);
        amigoRepository.save(amigoEntity);
    }

    public void rechazarPeticionAmistad(int id) {
        AmigoEntity amigoEntity = amigoRepository.findById(id);
        amigoRepository.delete(amigoEntity);
    }

    public void eliminarAmigo(int id) {
        AmigoEntity amigoEntity = amigoRepository.findById(id);
        amigoRepository.delete(amigoEntity);
    }

    public List<Amigo> getAmigos(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        List<Amigo> amigos = new ArrayList<>();
        List<AmigoEntity> amigosEntity = amigoRepository.findFriendsByUser(userEntity);
        amigosEntity.forEach(amigoEntity -> amigos.add(mapperService.toAmigo(amigoEntity)));
        List<AmigoEntity> pendingRequests = amigoRepository.findPendingRequestsByUser(userEntity);
        pendingRequests.forEach(amigoEntity -> amigos.add(mapperService.toAmigo(amigoEntity)));
        return amigos;
    }


}
