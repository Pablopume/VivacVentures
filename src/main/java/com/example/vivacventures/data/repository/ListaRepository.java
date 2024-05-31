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

    @Query("SELECT l.id, l.name, u.username FROM ListaEntity l JOIN l.user u WHERE l.id = :id")
    List<Object[]> findIdNameAndUsernameById(@Param("id") int listaId);

    @Query("SELECT f.vivacPlace.id FROM FavoritoEntity f WHERE f.lista.id = :listaId")
    List<Integer> findVivacPlaceIdsByListaId(@Param("listaId") int listaId);

    @Query("SELECT l.id, l.name FROM ListaEntity l WHERE (l.user.username = :username OR l.id IN (SELECT lu.lista.id FROM ListaUserEntity lu WHERE lu.user.username = :username)) AND l.id IN (SELECT f.lista.id FROM FavoritoEntity f WHERE f.vivacPlace.id = :vivacPlaceId)")
    List<Object[]> findIdAndNameByUsernameAndVivacPlaceId(@Param("username") String username, @Param("vivacPlaceId") int vivacPlaceId);
//Get listas shared with an specific user by id with the  lista_user table

    @Query("SELECT l FROM ListaEntity l JOIN l.listaUsers lu WHERE lu.user.id = :userId")
    List<ListaEntity> getByUserId(@Param("userId") int userId);
}
