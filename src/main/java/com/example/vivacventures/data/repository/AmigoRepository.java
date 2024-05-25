package com.example.vivacventures.data.repository;

import com.example.vivacventures.data.modelo.AmigoEntity;
import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.modelo.ValorationEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AmigoRepository extends ListCrudRepository<AmigoEntity, Long> {

    AmigoEntity findById(int id);

   List <AmigoEntity> findByRequester(UserEntity requestedUser);

}
