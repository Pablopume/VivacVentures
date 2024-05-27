package com.example.vivacventures.data.repository;

import com.example.vivacventures.data.modelo.AmigoEntity;
import com.example.vivacventures.data.modelo.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmigoRepository extends ListCrudRepository<AmigoEntity, Long> {

    AmigoEntity findById(int id);

    List<AmigoEntity> findByRequester(UserEntity requestedUser);

    @Query("SELECT a FROM AmigoEntity a WHERE (a.requester = :user OR a.requested = :user) AND a.status = true")
    List<AmigoEntity> findFriendsByUser(@Param("user") UserEntity user);

    @Query("SELECT a FROM AmigoEntity a WHERE (a.requested = :user) AND a.status = false")
    List<AmigoEntity> findPendingRequestsByUser(@Param("user") UserEntity user);


}
