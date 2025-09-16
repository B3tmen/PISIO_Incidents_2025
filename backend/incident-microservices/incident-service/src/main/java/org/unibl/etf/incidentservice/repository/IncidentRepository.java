package org.unibl.etf.incidentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.incidentservice.model.entity.IncidentEntity;

public interface IncidentRepository extends JpaRepository<IncidentEntity, Integer> {
}
