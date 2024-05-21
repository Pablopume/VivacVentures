package com.example.vivacventures.data.repository;

import com.example.vivacventures.data.modelo.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends ListCrudRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.valorations WHERE u.username = ?1")
    UserEntity findByUsername(String username);
    UserEntity findByRandomStringVerified(String randomStringVerified);
    @Modifying
    @Transactional
    @Query(""" 
            update UserEntity u set u.verified = true
            where u.randomStringVerified = :randomStringVerified
            """)
    void updateUserVerified(String randomStringVerified);

    @Modifying
    @Transactional
    @Query(""" 
            update UserEntity u set u.randomStringVerified = :randomStringVerified, u.verificationExpirationDate = :expirationDate
            where u.username = :username
            """)
    void updateUserStringVerified(String username, String randomStringVerified, LocalDateTime expirationDate);

    UserEntity findById(int id);

    UserEntity findByEmail(String email);

    @Query(value = "SELECT u.username, COUNT(vp.id) as num_vivac_places FROM user u LEFT JOIN vivac_place vp ON u.username = vp.username GROUP BY u.username", nativeQuery = true)
    List<Object[]> getAllUsersWithVivacPlaceCount();
}
