package org.unibl.etf.analyticsservice.rabbitmq.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.analyticsservice.model.entity.LocationEntity;
import org.unibl.etf.analyticsservice.model.enums.IncidentSubtype;
import org.unibl.etf.analyticsservice.model.enums.IncidentType;

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
    private LocationEntity location;
    private LocalDateTime timestamp;
}

