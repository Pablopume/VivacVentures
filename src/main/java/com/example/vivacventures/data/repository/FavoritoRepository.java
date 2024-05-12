package com.example.vivacventures.data.repository;

import com.example.vivacventures.data.modelo.FavoritoEntity;
import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.modelo.VivacPlaceEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FavoritoRepository extends ListCrudRepository<FavoritoEntity, Long> {

//    @Transactional
//    @Modifying
//    @Query("DELETE FROM FavoritoEntity f WHERE f.user = :user AND f.vivacPlace = :vivacPlace")
//    void deleteByUserAndVivacPlace(UserEntity user, VivacPlaceEntity vivacPlace);
    @Transactional
    @Modifying
    void deleteByUserAndVivacPlace(UserEntity user, VivacPlaceEntity vivacPlace);

    @Query("SELECT f FROM FavoritoEntity f LEFT JOIN FETCH f.vivacPlace vp LEFT JOIN FETCH vp.valorations WHERE f.user = :user")
    List<FavoritoEntity> findByUserFetch(UserEntity user);

    @Query(value = "SELECT vp.id, vp.name, vp.type, AVG(vr.score) as valorations, (SELECT url FROM image WHERE vivac_id = vp.id LIMIT 1) as images, (SELECT COUNT(*) FROM favorito WHERE user_id = u.id AND vivac_place_id = vp.id) > 0 as isFavorite FROM vivac_place vp LEFT JOIN valoration vr ON vp.id = vr.vivac_id INNER JOIN favorito f ON vp.id = f.vivac_place_id INNER JOIN user u ON f.user_id = u.id WHERE u.username = :username GROUP BY vp.id", nativeQuery = true)
    List<Object[]> getFavouritesVivacPlaces(@Param("username") String username);

}
