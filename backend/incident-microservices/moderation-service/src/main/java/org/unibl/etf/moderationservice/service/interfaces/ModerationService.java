package org.unibl.etf.moderationservice.service.interfaces;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.moderationservice.model.dto.IncidentDTO;
import org.unibl.etf.moderationservice.model.dto.IncidentModerationDTO;
import org.unibl.etf.moderationservice.model.enums.IncidentStatus;

import java.util.List;

public interface ModerationService {

    List<IncidentModerationDTO> getAllIncidents();

    IncidentStatus getIncidentStatus(Integer incidentId);

    IncidentModerationDTO updateIncidentStatus(Integer incidentId, IncidentStatus status, Integer moderatorId);

    IncidentDTO getIncident(Integer incidentId);
}
