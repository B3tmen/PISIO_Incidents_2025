package org.unibl.etf.moderationservice.model.request;


import org.unibl.etf.moderationservice.model.enums.IncidentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentStatusUpdateRequest {
    private IncidentStatus status;
}
