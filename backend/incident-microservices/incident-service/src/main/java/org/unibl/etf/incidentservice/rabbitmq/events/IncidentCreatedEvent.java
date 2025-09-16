package org.unibl.etf.incidentservice.rabbitmq.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.unibl.etf.incidentservice.model.dto.IncidentImageDTO;
import org.unibl.etf.incidentservice.model.entity.IncidentImageEntity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class IncidentCreatedEvent implements Serializable {
    private Integer incidentId;
    private List<IncidentImageDTO> images;
    private LocalDateTime timestamp;
}
