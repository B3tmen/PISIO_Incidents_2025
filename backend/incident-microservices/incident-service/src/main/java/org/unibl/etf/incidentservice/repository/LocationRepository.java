package org.unibl.etf.incidentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.incidentservice.model.entity.LocationEntity;

public interface LocationRepository extends JpaRepository<LocationEntity, Integer> {
}
