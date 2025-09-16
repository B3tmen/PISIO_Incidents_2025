package org.unibl.etf.analyticsservice.rabbitmq;


import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.unibl.etf.analyticsservice.model.entity.IncidentEntity;
import org.unibl.etf.analyticsservice.model.entity.LocationEntity;
import org.unibl.etf.analyticsservice.rabbitmq.events.IncidentAnalyticsCreatedEvent;
import org.unibl.etf.analyticsservice.repository.IncidentRepository;
import org.unibl.etf.analyticsservice.repository.LocationRepository;

@Slf4j
@Service
public class RabbitMQConsumer {
    private final IncidentRepository incidentRepository;
    private final LocationRepository locationRepository;

    public RabbitMQConsumer(IncidentRepository incidentRepository, LocationRepository locationRepository) {
        this.incidentRepository = incidentRepository;
        this.locationRepository = locationRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue-incident-created}")
    @Transactional
    public void handleIncidentCreated(IncidentAnalyticsCreatedEvent event) {
 
        log.info("[INCIDENT-CREATED] Received: incidentId={}, timestamp={}",
                event.getIncidentId(), event.getTimestamp());

        LocationEntity locationEntity = LocationEntity.builder()
                .latitude(event.getLocation().getLatitude())
                .longitude(event.getLocation().getLongitude())
                .radius(event.getLocation().getRadius())
                .address(event.getLocation().getAddress())
                .city(event.getLocation().getCity())
                .state(event.getLocation().getState())
                .country(event.getLocation().getCountry())
                .zipCode(event.getLocation().getZipCode())
                .build();
        locationEntity = locationRepository.save(locationEntity);

        IncidentEntity incidentEntity = IncidentEntity.builder()
                .type(event.getType())
                .subtype(event.getSubtype())
                .reportedAt(event.getReportedAt())
                .location(locationEntity)
                .build();
        incidentEntity = incidentRepository.save(incidentEntity);

        log.info("[INCIDENT-CREATED] Saved: incidentId={}, timestamp={}",
                incidentEntity.getId(), event.getTimestamp());
    }

}

