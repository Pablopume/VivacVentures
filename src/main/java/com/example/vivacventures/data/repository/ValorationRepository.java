package com.example.vivacventures.data.repository;

import com.example.vivacventures.data.modelo.FavoritoEntity;
import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.modelo.ValorationEntity;
import com.example.vivacventures.data.modelo.VivacPlaceEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ValorationRepository extends ListCrudRepository<ValorationEntity, Long> {

void deleteById(int id);
ValorationEntity findById(int id);

@Query(value = "INSERT INTO valoration (vivac_id, username, score, review) VALUES (:vivac_id, :username, :score, :review)", nativeQuery = true)
ValorationEntity insertValoration(@Param("vivac_id") int vivac_id, @Param("username") String username, @Param("score") int score, @Param("review") String review);
}
