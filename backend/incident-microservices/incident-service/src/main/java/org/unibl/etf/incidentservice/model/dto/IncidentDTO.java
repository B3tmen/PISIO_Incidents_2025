package org.unibl.etf.incidentservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.incidentservice.model.enums.IncidentStatus;
import org.unibl.etf.incidentservice.model.enums.IncidentSubtype;
import org.unibl.etf.incidentservice.model.enums.IncidentType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentDTO {
    private Integer id;
    private IncidentType type;
    private IncidentSubtype subtype;
    private IncidentStatus status;
    private LocalDateTime reportedAt;
    private String description;
    private List<IncidentImageDTO> images;
    private LocationDTO location;
}
