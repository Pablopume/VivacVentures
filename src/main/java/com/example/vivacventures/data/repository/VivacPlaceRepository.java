package com.example.vivacventures.data.repository;
import com.example.vivacventures.data.modelo.VivacPlaceEntity;
import jakarta.websocket.server.PathParam;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VivacPlaceRepository extends ListCrudRepository<VivacPlaceEntity, Long> {
    //find by type
    @Query("SELECT v FROM VivacPlaceEntity v  LEFT JOIN FETCH v.valorations WHERE v.type = ?1")
    List<VivacPlaceEntity> getVivacByType(String type);
    @Query("SELECT v FROM VivacPlaceEntity v LEFT JOIN FETCH v.valorations")
    List<VivacPlaceEntity> findAllWithVivacPlaceEntity();
    @Query("SELECT v FROM VivacPlaceEntity v JOIN FETCH v.valorations WHERE (6371 * acos(cos(radians(:userLatitude)) * cos(radians(v.latitude)) * cos(radians(v.longitude) - radians(:userLongitude)) + sin(radians(:userLatitude)) * sin(radians(v.latitude)))) <= 2 ")
    List<VivacPlaceEntity> findNearbyPlaces(@PathParam("userLatitude") double userLatitude, @PathParam("userLongitude") double userLongitude);


}

