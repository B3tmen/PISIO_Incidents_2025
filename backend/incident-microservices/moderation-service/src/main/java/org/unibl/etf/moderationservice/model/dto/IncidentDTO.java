package org.unibl.etf.moderationservice.model.dto;

import org.unibl.etf.moderationservice.model.enums.IncidentStatus;
import org.unibl.etf.moderationservice.model.enums.IncidentSubtype;
import org.unibl.etf.moderationservice.model.enums.IncidentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentDTO implements Serializable {

    private Long id;
    private IncidentType type;
    private IncidentSubtype subtype;
    private LocationDTO location;
    private String description;
    private List<IncidentImageDTO> images;
    private LocalDateTime reportedAt;
    private IncidentStatus status;
}

