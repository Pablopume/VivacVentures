package com.example.vivacventures.data.repository;

import com.example.vivacventures.data.modelo.AmigoEntity;
import com.example.vivacventures.data.modelo.ListaEntity;
import com.example.vivacventures.data.modelo.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListaRepository extends ListCrudRepository<ListaEntity, Long> {
//con fetch de la lista de favoritos
    @Query("SELECT l FROM ListaEntity l LEFT JOIN FETCH l.favoritos WHERE l.id = :id")
    ListaEntity findById(int id);
    void deleteById(int id);
    @Query("SELECT l FROM ListaEntity l LEFT JOIN FETCH l.favoritos  WHERE l.user = :user")
    List<ListaEntity> findAllByUser(@Param("user") UserEntity user);



}
