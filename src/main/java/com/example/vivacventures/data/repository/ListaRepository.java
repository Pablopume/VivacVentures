package com.example.vivacventures.data.repository;

import com.example.vivacventures.data.modelo.ListaEntity;
import com.example.vivacventures.domain.modelo.dto.ListaDTO;
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

    @Query("SELECT l.id, l.name FROM ListaEntity l JOIN l.user u WHERE u.username = :username")
    List<Object[]> findIdAndNameByUsername(@Param("username") String username);

    @Query("SELECT f.vivacPlace.id FROM FavoritoEntity f WHERE f.lista.id = :listaId")
    List<Integer> findVivacPlaceIdsByListaId(@Param("listaId") int listaId);


}
