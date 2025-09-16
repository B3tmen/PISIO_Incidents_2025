package org.unibl.etf.incidentservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.unibl.etf.incidentservice.model.dto.IncidentImageDTO;
import org.unibl.etf.incidentservice.model.dto.LocationDTO;
import org.unibl.etf.incidentservice.model.entity.IncidentEntity;
import org.unibl.etf.incidentservice.model.entity.IncidentImageEntity;
import org.unibl.etf.incidentservice.model.entity.LocationEntity;
import org.unibl.etf.incidentservice.model.enums.IncidentStatus;
import org.unibl.etf.incidentservice.model.enums.IncidentSubtype;
import org.unibl.etf.incidentservice.model.enums.IncidentType;
import org.unibl.etf.incidentservice.rabbitmq.RabbitMQProducer;
import org.unibl.etf.incidentservice.rabbitmq.events.IncidentAnalyticsCreatedEvent;
import org.unibl.etf.incidentservice.rabbitmq.events.IncidentCreatedEvent;
import org.unibl.etf.incidentservice.rabbitmq.factory.EventFactory;
import org.unibl.etf.incidentservice.repository.IncidentImageRepository;
import org.unibl.etf.incidentservice.repository.IncidentRepository;
import org.unibl.etf.incidentservice.repository.LocationRepository;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DatabaseMockupLoader {
    private final ModelMapper modelMapper;

    public DatabaseMockupLoader(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Bean
    public CommandLineRunner loadMockupData(
            IncidentRepository incidentRepository,
            LocationRepository locationRepository,
            RabbitMQProducer rabbitMQProducer,
            EventFactory eventFactory
    ) {
        return args -> {
            if(incidentRepository.count() == 0) {
                for (int i = 0; i < 10; i++) {
                    List<IncidentImageEntity> images = generateIncidentImages(i);
                    LocationEntity location = locationRepository.saveAndFlush(generateLocation(i));

                    IncidentEntity incident = IncidentEntity.builder()
                            .type(IncidentType.values()[i % IncidentType.values().length])
                            .subtype(i % 2 == 0 ? IncidentSubtype.values()[i % IncidentSubtype.values().length] : null)
                            .description("Test incident description " + i)
                            .location(location)
                            .images(new ArrayList<>())
                            .reportedAt(LocalDateTime.now())
                            .status(i % 2 == 0 ? IncidentStatus.APPROVED : IncidentStatus.PENDING)
                            .build();
                    images.forEach(incident::addImage);

                    IncidentEntity savedEntity = incidentRepository.saveAndFlush(incident);

                    // send to analytics-service
                    IncidentAnalyticsCreatedEvent analyticsCreatedEvent = eventFactory.createIncidentAnalyticsCreatedEvent(savedEntity);
                    rabbitMQProducer.sendIncidentAnalyticsCreatedEvent(analyticsCreatedEvent);
                    // send to moderation-service
                    IncidentCreatedEvent incidentCreatedEvent = eventFactory.createIncidentCreatedEvent(savedEntity);
                    rabbitMQProducer.sendIncidentCreatedEvent(incidentCreatedEvent);
                }
            }
        };
    }

    // There is an easter egg if you look at the pins these coordinates make on the map :)
    private LocationEntity generateLocation(int index) {
        return switch (index % 9) {
            case 0 ->
                    new LocationEntity(null, BigDecimal.valueOf(44.412650), BigDecimal.valueOf(17.085271), BigDecimal.valueOf(1000.0), "Sime Šolaje", "Mrkonjić Grad", "Republika Srpska", "Bosna i Hercegovina", "70260");
            case 1 ->
                    new LocationEntity(null, BigDecimal.valueOf(44.425280), BigDecimal.valueOf(18.038302), BigDecimal.valueOf(1000.0), "Sarajevska ulica", "Žepče", "Federacija Bosne i Hercegovine", "Bosna i Hercegovina", "72230");
            case 2 ->
                    new LocationEntity(null, BigDecimal.valueOf(44.046333), BigDecimal.valueOf(16.855193), BigDecimal.valueOf(1000.0), "Petra Svačića", "Glamoč", "Federacija Bosne i Hercegovine", "Bosna i Hercegovina", "80230");
            case 3 ->
                    new LocationEntity(null, BigDecimal.valueOf(43.827084), BigDecimal.valueOf(17.002059), BigDecimal.valueOf(1000.0), "Svetog Ive 2", "Livno", "Federacija Bosne i Hercegovine", "Bosna i Hercegovina", "80101");
            case 4 ->
                    new LocationEntity(null, BigDecimal.valueOf(43.718667), BigDecimal.valueOf(17.225379), BigDecimal.valueOf(1000.0), "Maka Dizdara St 12", "Tomislavgrad", "Federacija Bosne i Hercegovine", "Bosna i Hercegovina", "80240");

            case 5 ->
                    new LocationEntity(null, BigDecimal.valueOf(43.659637), BigDecimal.valueOf(17.761286), BigDecimal.valueOf(1000.0), "Jaroslava Černija", "Jablanica", "Federacija Bosne i Hercegovine", "Bosna i Hercegovina", "88420");
            case 6 ->
                    new LocationEntity(null, BigDecimal.valueOf(43.702267), BigDecimal.valueOf(17.992978), BigDecimal.valueOf(1000.0), "Konjic ulica", "Konjic", "Federacija Bosne i Hercegovine", "Bosna i Hercegovina", "88400");
            case 7 ->
                    new LocationEntity(null, BigDecimal.valueOf(43.852510), BigDecimal.valueOf(18.336588), BigDecimal.valueOf(1000.0), "Safeta Zajke 77-75", "Sarajevo", "Federacija Bosne i Hercegovine", "Bosna i Hercegovina", "71000");
            case 8 ->
                    new LocationEntity(null, BigDecimal.valueOf(43.966918), BigDecimal.valueOf(18.366174), BigDecimal.valueOf(1000.0), "M18", "M18", "Federacija Bosne i Hercegovine", "Bosna i Hercegovina", "71000");

            default ->
                    new LocationEntity(null, BigDecimal.valueOf(44.412650), BigDecimal.valueOf(17.085271), BigDecimal.valueOf(1000.0), "Sime Šolaje", "Mrkonjić Grad", "Republika Srpska", "Bosna i Hercegovina", "70260");
        };
    }

    private List<IncidentImageEntity> generateIncidentImages(int index) {
        String imageUrl = "https://placeholder.pics/svg/50?text=Image" + index;
        IncidentImageEntity imageEntity = new IncidentImageEntity();
        imageEntity.setImageURL(imageUrl);

        return List.of(imageEntity);
    }
}
