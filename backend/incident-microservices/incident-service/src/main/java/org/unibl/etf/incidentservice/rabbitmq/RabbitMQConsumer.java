package org.unibl.etf.incidentservice.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.unibl.etf.incidentservice.model.entity.IncidentEntity;
import org.unibl.etf.incidentservice.rabbitmq.events.IncidentStatusUpdateEvent;
import org.unibl.etf.incidentservice.repository.IncidentRepository;

import java.util.Optional;

@Service
public class RabbitMQConsumer {
    private final IncidentRepository incidentRepository;

    public RabbitMQConsumer(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue-status-updated}")
    public void receiveIncidentStatusUpdated(IncidentStatusUpdateEvent incidentStatusUpdateEvent) {
        System.out.println("------------ EVENT: " + incidentStatusUpdateEvent + " ----------------------------- ");

        Optional<IncidentEntity> incidentOptional = incidentRepository.findById(incidentStatusUpdateEvent.getIncidentId());

        if (incidentOptional.isPresent()) {
            IncidentEntity entity = incidentOptional.get();

            entity.setStatus(incidentStatusUpdateEvent.getStatus());
            incidentRepository.save(entity);

            System.out.println("Incident Status Updated Successfully: " + entity.getId());
        }
    }
}
