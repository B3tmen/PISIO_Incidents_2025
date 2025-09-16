package org.unibl.etf.analyticsservice.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.unibl.etf.analyticsservice.model.dto.IncidentDTO;
import org.unibl.etf.analyticsservice.model.entity.IncidentEntity;
import org.unibl.etf.analyticsservice.model.projections.DailyCountProjection;
import org.unibl.etf.analyticsservice.model.projections.LocationCountProjection;
import org.unibl.etf.analyticsservice.model.projections.TypeCountProjection;
import org.unibl.etf.analyticsservice.repository.IncidentRepository;

import java.util.List;

@Service
public class AnalyticsService {
    private final IncidentRepository repo;
    private final ModelMapper modelMapper;

    public AnalyticsService(IncidentRepository repo, ModelMapper modelMapper) {
        this.repo = repo;
        this.modelMapper = modelMapper;
    }

    public List<IncidentDTO> getAll() {
        return repo.findAll().stream().map(e -> modelMapper.map(e, IncidentDTO.class)).toList();
    }

    public List<TypeCountProjection> getIncidentCountByType() {
        return repo.countByType();
    }

    public List<DailyCountProjection> getIncidentCountByDay() {
        return repo.countByDay();
    }

    public List<LocationCountProjection> getIncidentCountByLocation() {
        return repo.countByLocation();
    }
}
