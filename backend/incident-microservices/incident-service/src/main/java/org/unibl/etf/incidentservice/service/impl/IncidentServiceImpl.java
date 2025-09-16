package org.unibl.etf.incidentservice.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.incidentservice.model.dto.IncidentDTO;
import org.unibl.etf.incidentservice.model.dto.IncidentImageDTO;
import org.unibl.etf.incidentservice.model.entity.IncidentEntity;
import org.unibl.etf.incidentservice.model.entity.IncidentImageEntity;
import org.unibl.etf.incidentservice.model.requests.IncidentRequest;
import org.unibl.etf.incidentservice.model.requests.TranslationRequest;
import org.unibl.etf.incidentservice.rabbitmq.RabbitMQProducer;
import org.unibl.etf.incidentservice.rabbitmq.events.IncidentAnalyticsCreatedEvent;
import org.unibl.etf.incidentservice.rabbitmq.events.IncidentCreatedEvent;
import org.unibl.etf.incidentservice.rabbitmq.factory.EventFactory;
import org.unibl.etf.incidentservice.repository.IncidentRepository;
import org.unibl.etf.incidentservice.repository.LocationRepository;
import org.unibl.etf.incidentservice.service.interfaces.AwsS3Service;
import org.unibl.etf.incidentservice.service.interfaces.IncidentService;
import org.unibl.etf.incidentservice.util.ImageHelper;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IncidentServiceImpl implements IncidentService {
    private final IncidentRepository incidentRepository;
    private final LocationRepository locationRepository;
    private final ModelMapper modelMapper;
    private final RabbitMQProducer rabbitMQProducer;
    private final AwsS3Service awsS3Service;
    private final EventFactory eventFactory;
    private final RestTemplate restTemplate;

    @Value("${translation.url}")
    private String translationUrl;
    @Value("${translation.api-key}")
    private String translationApiKey;

    public IncidentServiceImpl(IncidentRepository incidentRepository,
                               LocationRepository locationRepository,
                               ModelMapper modelMapper,
                               RabbitMQProducer rabbitMQProducer,
                               AwsS3Service awsS3Service,
                               EventFactory eventFactory, RestTemplate restTemplate
    ) {
        this.incidentRepository = incidentRepository;
        this.locationRepository = locationRepository;
        this.modelMapper = modelMapper;
        this.rabbitMQProducer = rabbitMQProducer;
        this.awsS3Service = awsS3Service;
        this.eventFactory = eventFactory;
        this.restTemplate = restTemplate;
    }


    @Override
    public IncidentDTO insertIncident(IncidentRequest incidentRequest, MultipartFile[] images) {
        IncidentEntity incidentEntity = modelMapper.map(incidentRequest, IncidentEntity.class);
        incidentEntity.setReportedAt(LocalDateTime.now());

        saveAndFlushIncidentTransitiveValues(incidentEntity, images);
        incidentEntity = incidentRepository.saveAndFlush(incidentEntity);

        // Send event(s) that new incident has been created
        IncidentCreatedEvent incidentCreatedEvent = eventFactory.createIncidentCreatedEvent(incidentEntity);
        rabbitMQProducer.sendIncidentCreatedEvent(incidentCreatedEvent);
        IncidentAnalyticsCreatedEvent analyticsCreatedEvent = eventFactory.createIncidentAnalyticsCreatedEvent(incidentEntity);
        rabbitMQProducer.sendIncidentAnalyticsCreatedEvent(analyticsCreatedEvent);

        return modelMapper.map(incidentEntity, IncidentDTO.class);
    }

    @Override
    public IncidentDTO getById(int id) {
        return null;
    }

    @Override
    public List<IncidentDTO> getAllIncidents() {
        List<IncidentEntity> incidentEntities = incidentRepository.findAll();
        ImageHelper.setupImageURLs(incidentEntities, awsS3Service);

        return incidentEntities.stream()
                .map((ie) -> modelMapper.map(ie, IncidentDTO.class))
                .toList();
    }

    @Override
    public String translateText(TranslationRequest translationRequest) {
        Map<String, Object> body = new HashMap<>();
        body.put("q", translationRequest.getText());
        body.put("source", translationRequest.getSourceLang());
        body.put("target", translationRequest.getTargetLang());
        body.put("format", "text");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // Make request
        ResponseEntity<Map> response = restTemplate.exchange(
                translationUrl + "?key=" + translationApiKey,
                HttpMethod.POST,
                entity,
                Map.class
        );

        // Parse response
        Map data = (Map) response.getBody().get("data");
        Map firstTranslation = (Map) ((java.util.List<?>) data.get("translations")).get(0);
        return (String) firstTranslation.get("translatedText");
    }

    private void saveAndFlushIncidentTransitiveValues(IncidentEntity incidentEntity, MultipartFile[] images) {
        List<IncidentImageDTO> incidentImageDTOs = new ArrayList<>();
        if(images != null) {
            try {
                Instant start = Instant.now();
                incidentImageDTOs = awsS3Service.uploadFiles(images);
                Instant end = Instant.now();

                System.out.println("\n ------------------- AWS duration: " + Duration.between(start, end).toMillis() + " ms ------------------------- \n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (IncidentImageDTO incidentImageDTO : incidentImageDTOs) {
            incidentEntity.addImage(modelMapper.map(incidentImageDTO, IncidentImageEntity.class));
        }

        locationRepository.saveAndFlush(incidentEntity.getLocation());
    }


}
