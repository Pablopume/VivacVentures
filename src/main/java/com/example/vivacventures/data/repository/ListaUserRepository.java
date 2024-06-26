package com.example.vivacventures.data.repository;

import com.example.vivacventures.data.modelo.ListaEntity;
import com.example.vivacventures.data.modelo.ListaUserEntity;
import com.example.vivacventures.data.modelo.UserEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListaUserRepository extends ListCrudRepository<ListaUserEntity, Long> {

    ListaUserEntity findById(int id);

    List<ListaUserEntity> findByLista(ListaEntity lista);
    List<ListaUserEntity> findByUser(UserEntity user);

    void deleteById(int id);

    void deleteByListaAndUser(ListaEntity lista, UserEntity user);
boolean existsByListaAndUser(ListaEntity lista, UserEntity user);
}
