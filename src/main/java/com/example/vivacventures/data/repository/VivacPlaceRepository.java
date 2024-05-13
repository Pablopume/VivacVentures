package com.example.vivacventures.data.repository;
import com.example.vivacventures.data.modelo.VivacPlaceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VivacPlaceRepository extends ListCrudRepository<VivacPlaceEntity, Long> {


    @Query(value = "SELECT vp.id, vp.name, vp.type, AVG(vr.score) as valorations, (SELECT url FROM image WHERE vivac_id = vp.id LIMIT 1) as images, (SELECT COUNT(*) FROM favorito f INNER JOIN user u ON f.user_id = u.id WHERE u.username = :username AND f.vivac_place_id = vp.id) > 0 as isFavorite FROM vivac_place vp LEFT JOIN valoration vr ON vp.id = vr.vivac_id WHERE vp.type = :type GROUP BY vp.id", nativeQuery = true)
    List<Object[]> getVivacByTypeAndUser(String type, String username);

    @Query(value = "SELECT vp.id, vp.name, vp.type, AVG(vr.score) as valorations, (SELECT url FROM image WHERE vivac_id = vp.id LIMIT 1) as images, (SELECT COUNT(*) FROM favorito f INNER JOIN user u ON f.user_id = u.id WHERE u.username = :username AND f.vivac_place_id = vp.id) > 0 as isFavorite FROM vivac_place vp LEFT JOIN valoration vr ON vp.id = vr.vivac_id WHERE vp.username =:username  GROUP BY vp.id", nativeQuery = true)
    List<Object[]> getVivacByUser(String username);
    @Query("SELECT v FROM  VivacPlaceEntity v" )
    List<VivacPlaceEntity> findAllWithVivacPlaceEntity();
    @Query(value="SELECT vp.id, vp.name, vp.type, AVG(vr.score) as valorations, (SELECT url FROM image WHERE vivac_id = vp.id LIMIT 1) as images, (SELECT COUNT(*) FROM favorito f INNER JOIN user u ON f.user_id = u.id WHERE u.username = :username AND f.vivac_place_id = vp.id) > 0 as isFavorite FROM vivac_place vp LEFT JOIN valoration vr ON vp.id = vr.vivac_id GROUP BY vp.id ORDER BY ST_Distance(point(vp.latitude, vp.longitude), point(:userLatitude, :userLongitude))*111.32", nativeQuery = true)
    List<Object[]> findNearbyPlacesAndUser(@Param("userLatitude") double userLatitude, @Param("userLongitude") double userLongitude, @Param("username") String username);

    @Query("SELECT v FROM VivacPlaceEntity v where v.id = ?1")
    VivacPlaceEntity getVivacPlaceEntitiesById(int id);

    @Query(value = "SELECT vp.id, vp.name, vp.type, AVG(vr.score) as valorations, (SELECT url FROM image WHERE vivac_id = vp.id LIMIT 1) as images, (SELECT COUNT(*) FROM favorito f INNER JOIN user u ON f.user_id = u.id WHERE u.username = :username AND f.vivac_place_id = vp.id) > 0 as isFavorite FROM vivac_place vp LEFT JOIN valoration vr ON vp.id = vr.vivac_id GROUP BY vp.id", nativeQuery = true)
    List<Object[]> getVivacPlaceWithFavourites(@Param("username") String username);

    void deleteById(int id);

    @Query(value = "SELECT vr.id, u.username, vr.score, vr.review FROM valoration vr INNER JOIN user u ON vr.username = u.id WHERE vr.vivac_id = :id", nativeQuery = true)
    List<Object[]> findValorationsByVivacPlaceId(@Param("id") int id);

    @Query(value = "SELECT url FROM image WHERE vivac_id = :id", nativeQuery = true)
    List<String> findImagesByVivacPlaceId(@Param("id") int id);

    @Query(value = "SELECT vp.id, vp.name, vp.type, vp.description, vp.latitude, vp.longitude, vp.username, vp.capacity, vp.date, vp.price, (SELECT COUNT(*) FROM favorito f INNER JOIN user u ON f.user_id = u.id WHERE u.username = :username AND f.vivac_place_id = vp.id) > 0 as isFavorite FROM vivac_place vp WHERE vp.id = :id", nativeQuery = true)
    List<Object[]> findVivacPlaceByIdAndUsername(@Param("id") int id, @Param("username") String username);




}

