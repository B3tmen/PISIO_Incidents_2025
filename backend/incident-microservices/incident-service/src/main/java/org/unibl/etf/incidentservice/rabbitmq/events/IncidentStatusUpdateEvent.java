package org.unibl.etf.incidentservice.rabbitmq.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.incidentservice.model.enums.IncidentStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncidentStatusUpdateEvent implements Serializable {
    private Integer incidentId;
    private IncidentStatus status;
    private LocalDateTime timestamp;
}
