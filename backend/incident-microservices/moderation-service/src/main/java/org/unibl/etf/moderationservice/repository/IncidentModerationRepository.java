package org.unibl.etf.moderationservice.repository;

import org.unibl.etf.moderationservice.model.entity.IncidentModerationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.moderationservice.model.enums.IncidentStatus;

import java.util.List;
import java.util.Optional;

public interface IncidentModerationRepository extends JpaRepository<IncidentModerationEntity, Integer> {

    List<IncidentModerationEntity> findByStatus(IncidentStatus status);
    Optional<IncidentModerationEntity> findByIncidentId(Integer incidentId);
}
