package org.unibl.etf.incidentservice.rabbitmq.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.incidentservice.model.dto.LocationDTO;
import org.unibl.etf.incidentservice.model.enums.IncidentSubtype;
import org.unibl.etf.incidentservice.model.enums.IncidentType;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncidentAnalyticsCreatedEvent implements Serializable {
    private Integer incidentId;
    private IncidentType type;
    private IncidentSubtype subtype;
    private LocalDateTime reportedAt;
    private LocationDTO location;
    private LocalDateTime timestamp;
}

