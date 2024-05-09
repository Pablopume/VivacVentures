package com.example.vivacventures.data.repository;

import com.example.vivacventures.data.modelo.FavoritoEntity;
import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.modelo.VivacPlaceEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
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

}
