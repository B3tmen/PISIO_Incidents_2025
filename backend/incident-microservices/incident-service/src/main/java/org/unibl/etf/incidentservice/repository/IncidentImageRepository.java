package org.unibl.etf.incidentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.incidentservice.model.entity.IncidentImageEntity;

public interface IncidentImageRepository extends JpaRepository<IncidentImageEntity, Integer> {
}
