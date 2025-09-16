package org.unibl.etf.moderationservice.repository;

import org.unibl.etf.moderationservice.model.entity.IncidentStatusHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentStatusHistoryRepository extends JpaRepository<IncidentStatusHistoryEntity, Integer> {
}
