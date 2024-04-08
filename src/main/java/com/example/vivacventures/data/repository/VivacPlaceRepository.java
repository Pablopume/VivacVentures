package com.example.vivacventures.data.repository;
import com.example.vivacventures.data.modelo.VivacPlaceEntity;
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
}

