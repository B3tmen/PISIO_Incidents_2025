package org.unibl.etf.moderationservice.rabbitmq;


import org.unibl.etf.moderationservice.model.dto.IncidentImageDTO;
import org.unibl.etf.moderationservice.model.entity.IncidentImageEntity;
import org.unibl.etf.moderationservice.model.entity.IncidentModerationEntity;
import org.unibl.etf.moderationservice.model.entity.IncidentStatusHistoryEntity;
import org.unibl.etf.moderationservice.model.enums.IncidentStatus;
import org.unibl.etf.moderationservice.repository.IncidentImageRepository;
import org.unibl.etf.moderationservice.repository.IncidentModerationRepository;
import org.unibl.etf.moderationservice.repository.IncidentStatusHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.unibl.etf.moderationservice.rabbitmq.events.IncidentCreatedEvent;
import org.unibl.etf.moderationservice.rabbitmq.events.IncidentStatusUpdateEvent;
import org.unibl.etf.moderationservice.service.interfaces.AwsS3Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RabbitMQConsumer {

    private final RabbitMQProducer rabbitMQProducer;
    private final IncidentModerationRepository incidentModerationRepository;
    private final IncidentStatusHistoryRepository incidentStatusHistoryRepository;
    private final ModelMapper modelMapper;

    public RabbitMQConsumer(RabbitMQProducer rabbitMQProducer,
                            IncidentModerationRepository incidentModerationRepository,
                            IncidentStatusHistoryRepository incidentStatusHistoryRepository,
                            ModelMapper modelMapper
    ) {
        this.rabbitMQProducer = rabbitMQProducer;
        this.incidentModerationRepository = incidentModerationRepository;
        this.incidentStatusHistoryRepository = incidentStatusHistoryRepository;
        this.modelMapper = modelMapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue-incident-created}")
    @Transactional
    public void handleIncidentCreated(IncidentCreatedEvent event) {
 
        log.info("[INCIDENT-CREATED] Received: incidentId={}, images={}, timestamp={}",
                event.getIncidentId(), event.getImages(), event.getTimestamp());

        IncidentModerationEntity moderation = IncidentModerationEntity.builder()
                .incidentId(event.getIncidentId())
                .status(IncidentStatus.PENDING)
                .images(new ArrayList<>())
                .statusChangeTime(event.getTimestamp())
                .moderatorId(1)
                .build();
        for (IncidentImageDTO image : event.getImages()) {
            image.setId(null);
            IncidentImageEntity imageEntity = modelMapper.map(image, IncidentImageEntity.class);
            System.out.println("RECEIVED IMAGE IN RABBIT: " + imageEntity);
            moderation.addImage(imageEntity);
        }
        System.out.println("SAVING INCIDENT MODERATION: ");
        incidentModerationRepository.saveAndFlush(moderation);

        IncidentStatusHistoryEntity statusHistory = IncidentStatusHistoryEntity.builder()
                .incidentId(event.getIncidentId())
                .status(IncidentStatus.PENDING)
                .statusChangeTime(event.getTimestamp())
                .moderatorId(1)
                .build();
        incidentStatusHistoryRepository.saveAndFlush(statusHistory);

        IncidentStatusUpdateEvent statusEvent = new IncidentStatusUpdateEvent(
                moderation.getIncidentId(),
                IncidentStatus.PENDING,
                LocalDateTime.now()
        );
        rabbitMQProducer.sendIncidentStatusUpdatedEvent(statusEvent);
    }

}

