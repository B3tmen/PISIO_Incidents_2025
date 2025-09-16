package org.unibl.etf.analyticsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.analyticsservice.model.entity.LocationEntity;

public interface LocationRepository extends JpaRepository<LocationEntity, Integer> {
}
