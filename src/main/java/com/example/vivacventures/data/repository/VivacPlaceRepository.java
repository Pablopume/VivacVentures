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

    @Query("SELECT v FROM VivacPlaceEntity v LEFT JOIN FETCH v.valorations WHERE v.username = ?1")
    List<VivacPlaceEntity> getVivacByUser(String username);

    @Query("SELECT v FROM VivacPlaceEntity v LEFT JOIN FETCH v.valorations")
    List<VivacPlaceEntity> findAllWithVivacPlaceEntity();
    @Query("SELECT v FROM VivacPlaceEntity v LEFT JOIN FETCH v.valorations ORDER BY ST_Distance(point(v.latitude, v.longitude), point(:userLatitude, :userLongitude))*111.32")
    List<VivacPlaceEntity> findNearbyPlaces(@Param("userLatitude") double userLatitude, @Param("userLongitude") double userLongitude);

    @Query("SELECT v FROM VivacPlaceEntity v LEFT JOIN FETCH v.valorations WHERE v.id = ?1")
    VivacPlaceEntity getVivacPlaceEntitiesById(int id);


    void deleteById(int id);



}

