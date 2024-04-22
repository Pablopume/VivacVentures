package com.example.vivacventures.data.repository;
import com.example.vivacventures.data.modelo.VivacPlaceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VivacPlaceRepository extends ListCrudRepository<VivacPlaceEntity, Long> {
    //find by type
    @Query("SELECT v FROM VivacPlaceEntity v  LEFT JOIN FETCH v.valorations WHERE v.type = ?1")
    List<VivacPlaceEntity> getVivacByType(String type);
    @Query("SELECT v FROM VivacPlaceEntity v LEFT JOIN FETCH v.valorations")
    List<VivacPlaceEntity> findAllWithVivacPlaceEntity();
    @Query("SELECT v FROM VivacPlaceEntity v LEFT JOIN FETCH v.valorations LEFT JOIN FETCH v.images WHERE ST_Distance(point(v.latitude, v.longitude), point(:userLatitude, :userLongitude))*111.32 <= 2")
    List<VivacPlaceEntity> findNearbyPlaces(@Param("userLatitude") double userLatitude, @Param("userLongitude") double userLongitude);

    @Query("SELECT v FROM VivacPlaceEntity v LEFT JOIN FETCH v.valorations LEFT JOIN FETCH v.images WHERE v.id = ?1")
    VivacPlaceEntity getVivacPlaceEntitiesById(int id);

}

