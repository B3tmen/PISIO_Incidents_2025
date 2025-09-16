package org.unibl.etf.moderationservice.model.dto;

import org.unibl.etf.moderationservice.model.enums.IncidentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentModerationDTO implements Serializable {
    private Integer id;
    private Integer incidentId;
    private IncidentStatus status;
    private LocalDateTime statusChangeTime;
    private Long moderatorId;
    private List<IncidentImageDTO> images;
}
