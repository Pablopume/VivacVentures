package com.example.vivacventures.data.repository;

import com.example.vivacventures.data.modelo.ImageEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends ListCrudRepository<ImageEntity, Long> {

    @Query("SELECT i FROM ImageEntity i WHERE i.vivacPlaceEntity.id = ?1")
    List<ImageEntity> getByVivacPlaceEntity(int vivacPlaceEntityId);

    ImageEntity save(ImageEntity imageEntity);
}
