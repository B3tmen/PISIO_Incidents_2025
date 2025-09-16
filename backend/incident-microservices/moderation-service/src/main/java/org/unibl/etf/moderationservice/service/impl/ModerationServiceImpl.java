package org.unibl.etf.moderationservice.service.impl;

import org.unibl.etf.moderationservice.rabbitmq.RabbitMQProducer;
import org.unibl.etf.moderationservice.rabbitmq.events.IncidentStatusUpdateEvent;
import org.unibl.etf.moderationservice.exception.NotFoundException;
import org.unibl.etf.moderationservice.model.dto.IncidentDTO;
import org.unibl.etf.moderationservice.model.dto.IncidentModerationDTO;
import org.unibl.etf.moderationservice.model.entity.IncidentModerationEntity;
import org.unibl.etf.moderationservice.model.entity.IncidentStatusHistoryEntity;
import org.unibl.etf.moderationservice.model.enums.IncidentStatus;
import org.unibl.etf.moderationservice.repository.IncidentModerationRepository;
import org.unibl.etf.moderationservice.repository.IncidentStatusHistoryRepository;
import org.unibl.etf.moderationservice.service.interfaces.AwsS3Service;
import org.unibl.etf.moderationservice.service.interfaces.ModerationService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ModerationServiceImpl implements ModerationService {

    private final IncidentModerationRepository incidentModerationRepository;
    private final IncidentStatusHistoryRepository incidentStatusHistoryRepository;
    private final AwsS3Service awsS3Service;
    private final ModelMapper modelMapper;
    private final RabbitMQProducer rabbitMQProducer;
    private final WebClient.Builder webClientBuilder;

    public ModerationServiceImpl(IncidentModerationRepository incidentModerationRepository, IncidentStatusHistoryRepository incidentStatusHistoryRepository, AwsS3Service awsS3Service, ModelMapper modelMapper, RabbitMQProducer rabbitMQProducer, WebClient.Builder webClientBuilder) {
        this.incidentModerationRepository = incidentModerationRepository;
        this.incidentStatusHistoryRepository = incidentStatusHistoryRepository;
        this.awsS3Service = awsS3Service;
        this.modelMapper = modelMapper;
        this.rabbitMQProducer = rabbitMQProducer;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public List<IncidentModerationDTO> getAllIncidents() {
        List<IncidentModerationEntity> entities = incidentModerationRepository.findAll();

        setupImageURLs(entities);

        return entities.stream()
                .map(e -> modelMapper.map(e, IncidentModerationDTO.class))
                .toList();
    }

    @Override
    public IncidentStatus getIncidentStatus(Integer incidentId) {
        return incidentModerationRepository.findByIncidentId(incidentId).orElseThrow(NotFoundException::new).getStatus();
    }

    @Override
    @Transactional
    public IncidentModerationDTO updateIncidentStatus(Integer incidentId, IncidentStatus status, Integer moderatorId) {

        IncidentModerationEntity incidentModerationEntity = incidentModerationRepository.findByIncidentId(incidentId).orElseThrow(() -> new NotFoundException("Incident not found with ID: " + incidentId));

        incidentModerationEntity.setStatus(status);
        incidentModerationEntity.setModeratorId(moderatorId);
        incidentModerationEntity.setStatusChangeTime(LocalDateTime.now());
        incidentModerationEntity = incidentModerationRepository.save(incidentModerationEntity);


        IncidentStatusHistoryEntity statusHistory = IncidentStatusHistoryEntity.builder().incidentId(incidentId).status(status).statusChangeTime(LocalDateTime.now()).moderatorId(moderatorId).build();
        incidentStatusHistoryRepository.save(statusHistory);

        // Send event to RabbitMQ about incident status update
        IncidentStatusUpdateEvent statusEvent = new IncidentStatusUpdateEvent(incidentId, status, LocalDateTime.now());
        rabbitMQProducer.sendIncidentStatusUpdatedEvent(statusEvent);

        return modelMapper.map(incidentModerationEntity, IncidentModerationDTO.class);
    }

    @Override
    public IncidentDTO getIncident(Integer incidentId) {
        WebClient incidentClient = webClientBuilder.baseUrl("http://incident-service/api/v1/incidents").build();

        return incidentClient.get().uri("/{id}", incidentId).retrieve().bodyToMono(IncidentDTO.class).block();
    }

    public void setupImageURLs(List<IncidentModerationEntity> incidentEntities) {
        incidentEntities.stream()
                .flatMap(entity -> entity.getImages().stream())
                .forEach(image ->
                        image.setImageURL(awsS3Service.getPresignedUrl(image.getImageURL()))
                );
    }
}

