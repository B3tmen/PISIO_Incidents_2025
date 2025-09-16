package org.unibl.etf.incidentservice.rabbitmq.factory;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.unibl.etf.incidentservice.model.dto.IncidentImageDTO;
import org.unibl.etf.incidentservice.model.dto.LocationDTO;
import org.unibl.etf.incidentservice.model.entity.IncidentEntity;
import org.unibl.etf.incidentservice.rabbitmq.events.IncidentAnalyticsCreatedEvent;
import org.unibl.etf.incidentservice.rabbitmq.events.IncidentCreatedEvent;

import java.time.LocalDateTime;

@Component
public class EventFactoryImpl implements EventFactory {
    private final ModelMapper modelMapper;

    public EventFactoryImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public IncidentCreatedEvent createIncidentCreatedEvent(IncidentEntity incidentEntity) {
        return IncidentCreatedEvent.builder()
                .incidentId(incidentEntity.getId())
                .images(
                        incidentEntity.getImages().stream()
                                .map(image -> modelMapper.map(image, IncidentImageDTO.class))
                                .toList()
                )
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public IncidentAnalyticsCreatedEvent createIncidentAnalyticsCreatedEvent(IncidentEntity incidentEntity) {
        return IncidentAnalyticsCreatedEvent.builder()
                .incidentId(incidentEntity.getId())
                .type(incidentEntity.getType())
                .subtype(incidentEntity.getSubtype())
                .reportedAt(incidentEntity.getReportedAt())
                .location(
                        modelMapper.map(incidentEntity.getLocation(), LocationDTO.class)
                )
                .timestamp(LocalDateTime.now())
                .build();
    }
}
