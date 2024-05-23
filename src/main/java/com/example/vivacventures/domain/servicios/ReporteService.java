package com.example.vivacventures.domain.servicios;

import com.example.vivacventures.data.modelo.FavoritoEntity;
import com.example.vivacventures.data.modelo.ReporteEntity;
import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.modelo.VivacPlaceEntity;
import com.example.vivacventures.data.modelo.mappers.VivacEntityMapper;
import com.example.vivacventures.data.repository.FavoritoRepository;
import com.example.vivacventures.data.repository.ReporteRepository;
import com.example.vivacventures.data.repository.UserRepository;
import com.example.vivacventures.data.repository.VivacPlaceRepository;
import com.example.vivacventures.domain.common.DomainConstants;
import com.example.vivacventures.domain.modelo.FavoritesVivacPlaces;
import com.example.vivacventures.domain.modelo.Report;
import com.example.vivacventures.domain.modelo.exceptions.NoExisteException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteService {
    private final VivacPlaceRepository vivacPlaceRepository;
    private final MapperService mapperService;
    private final ReporteRepository reporteRepository;
    private final UserRepository userRepository;

    public void save(Report report) {

        ReporteEntity reporteEntity1= reporteRepository.findByUsername(report.getUsername());
        if(reporteEntity1!=null){
            throw new NoExisteException("Ya has reportado un lugar");
        }
        VivacPlaceEntity vivacPlaceEntity = vivacPlaceRepository.getVivacPlaceEntitiesById(report.getVivacPlaceId());

        if (vivacPlaceEntity == null) {
            throw new NoExisteException("No existe el lugar con id " + report.getVivacPlaceId());
        }
        ReporteEntity reporteEntity = mapperService.toReportEntity(report);
        reporteRepository.save(reporteEntity);
        if (reporteRepository.countReportsByVivacPlaceId(report.getVivacPlaceId()) >= 3) {
            vivacPlaceEntity.setVisible(false);
            vivacPlaceRepository.save(vivacPlaceEntity);
        }
    }

    public void delete(int id) {
        reporteRepository.deleteById(id);
    }

}
