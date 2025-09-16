package org.unibl.etf.moderationservice.rabbitmq.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.moderationservice.model.dto.IncidentImageDTO;
import org.unibl.etf.moderationservice.model.entity.IncidentImageEntity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncidentCreatedEvent implements Serializable {
    private Integer incidentId;
    private List<IncidentImageDTO> images;
    private LocalDateTime timestamp;
}

