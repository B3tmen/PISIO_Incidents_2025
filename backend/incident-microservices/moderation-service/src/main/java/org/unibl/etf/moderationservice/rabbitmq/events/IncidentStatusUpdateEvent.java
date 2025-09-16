package org.unibl.etf.moderationservice.rabbitmq.events;

import org.unibl.etf.moderationservice.model.enums.IncidentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

