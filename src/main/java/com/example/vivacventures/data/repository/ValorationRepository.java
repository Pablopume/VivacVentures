package com.example.vivacventures.data.repository;

import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.modelo.ValorationEntity;
import com.example.vivacventures.data.modelo.VivacPlaceEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ValorationRepository extends ListCrudRepository<ValorationEntity, Long> {

    void deleteById(int id);

    ValorationEntity findById(int id);
    List<ValorationEntity> findByVivacPlaceEntity(VivacPlaceEntity vivacPlaceEntity);
    boolean existsByUserEntityAndVivacPlaceEntity(UserEntity userEntity, VivacPlaceEntity vivacPlaceEntity);
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO valoration (vivac_id, username, score, review, date) VALUES (:vivac_id, :username, :score, :review, :date)", nativeQuery = true)
    void insertValoration(@Param("vivac_id") int vivacId, @Param("username") int username, @Param("score") int score, @Param("review") String review, @Param("date") LocalDate date);
}
