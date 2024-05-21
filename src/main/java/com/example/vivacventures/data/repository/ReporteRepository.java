package com.example.vivacventures.data.repository;

import com.example.vivacventures.data.modelo.FavoritoEntity;
import com.example.vivacventures.data.modelo.ReporteEntity;
import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.modelo.VivacPlaceEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReporteRepository extends ListCrudRepository<ReporteEntity, Long> {

void deleteById(int id);

//query que cuente los reportes de un lugar
@Query(value = "SELECT COUNT(*) FROM reporte WHERE vivac_place_id = :vivac_place_id", nativeQuery = true)
int countReportsByVivacPlaceId(@Param("vivac_place_id") int vivac_place_id);

}
