package org.unibl.etf.moderationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.moderationservice.model.entity.IncidentImageEntity;

public interface IncidentImageRepository extends JpaRepository<IncidentImageEntity, Integer> {
}
