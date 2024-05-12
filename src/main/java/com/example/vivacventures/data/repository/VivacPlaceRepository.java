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

    @Query(value = "SELECT vp.id, vp.name, vp.type, AVG(vr.score) as valorations, (SELECT url FROM image WHERE vivac_id = vp.id LIMIT 1) as images, (SELECT COUNT(*) FROM favorito f INNER JOIN user u ON f.user_id = u.id WHERE u.username = :username AND f.vivac_place_id = vp.id) > 0 as isFavorite FROM vivac_place vp LEFT JOIN valoration vr ON vp.id = vr.vivac_id WHERE vp.username =:username  GROUP BY vp.id", nativeQuery = true)
    List<Object[]> getVivacByUser(String username);

    @Query("SELECT v FROM VivacPlaceEntity v LEFT JOIN FETCH v.valorations")
    List<VivacPlaceEntity> findAllWithVivacPlaceEntity();
    @Query("SELECT v FROM VivacPlaceEntity v LEFT JOIN FETCH v.valorations ORDER BY ST_Distance(point(v.latitude, v.longitude), point(:userLatitude, :userLongitude))*111.32")
    List<VivacPlaceEntity> findNearbyPlaces(@Param("userLatitude") double userLatitude, @Param("userLongitude") double userLongitude);

    @Query("SELECT v FROM VivacPlaceEntity v LEFT JOIN FETCH v.valorations WHERE v.id = ?1")
    VivacPlaceEntity getVivacPlaceEntitiesById(int id);

    @Query(value = "SELECT vp.id, vp.name, vp.type, AVG(vr.score) as valorations, (SELECT url FROM image WHERE vivac_id = vp.id LIMIT 1) as images, (SELECT COUNT(*) FROM favorito f INNER JOIN user u ON f.user_id = u.id WHERE u.username = :username AND f.vivac_place_id = vp.id) > 0 as isFavorite FROM vivac_place vp LEFT JOIN valoration vr ON vp.id = vr.vivac_id GROUP BY vp.id", nativeQuery = true)
    List<Object[]> getVivacPlaceWithFavourites(@Param("username") String username);

    void deleteById(int id);



}

