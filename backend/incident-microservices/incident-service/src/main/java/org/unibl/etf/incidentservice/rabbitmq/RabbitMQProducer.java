package org.unibl.etf.incidentservice.rabbitmq;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.unibl.etf.incidentservice.rabbitmq.events.IncidentAnalyticsCreatedEvent;
import org.unibl.etf.incidentservice.rabbitmq.events.IncidentCreatedEvent;

@Service
public class RabbitMQProducer {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties rabbitMQProperties;
    private final TopicExchange topicExchange;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate, RabbitMQProperties rabbitMQProperties, TopicExchange topicExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQProperties = rabbitMQProperties;
        this.topicExchange = topicExchange;
    }

    public void sendIncidentCreatedEvent(IncidentCreatedEvent incidentCreatedEvent) {
        try{
            rabbitTemplate.convertAndSend(topicExchange.getName(), rabbitMQProperties.getRoutingKeyIncidentCreated(), incidentCreatedEvent);
            System.out.println("Incident Created Event sent: " + incidentCreatedEvent);
        } catch (AmqpException e) {
            e.printStackTrace();
        }

    }

    public void sendIncidentAnalyticsCreatedEvent(IncidentAnalyticsCreatedEvent incidentAnalyticsCreatedEvent) {
        try{
            rabbitTemplate.convertAndSend(topicExchange.getName(), rabbitMQProperties.getRoutingKeyIncidentCreated(), incidentAnalyticsCreatedEvent);
            System.out.println("Incident Analytics Created Event sent: " + incidentAnalyticsCreatedEvent);
        } catch (AmqpException e) {
            e.printStackTrace();
        }

    }
}
