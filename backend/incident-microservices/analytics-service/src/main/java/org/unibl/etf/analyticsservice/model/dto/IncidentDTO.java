package org.unibl.etf.analyticsservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.analyticsservice.model.enums.IncidentSubtype;
import org.unibl.etf.analyticsservice.model.enums.IncidentType;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncidentDTO {
    private Integer id;
    private IncidentType type;
    private IncidentSubtype subtype;
    private LocalDateTime reportedAt;
    private LocationDTO location;
}

