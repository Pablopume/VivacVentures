package com.example.vivacventures.data.repository;
import com.example.vivacventures.data.modelo.VivacPlaceEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VivacPlaceRepository extends ListCrudRepository<VivacPlaceEntity, Long> {
    //find by type
    List<VivacPlaceEntity> findByType(String type);
}
