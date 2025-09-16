package org.unibl.etf.incidentservice.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.incidentservice.model.enums.IncidentStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentStatusUpdateRequest {
    private IncidentStatus status;
}
