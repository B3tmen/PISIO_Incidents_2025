package org.unibl.etf.incidentservice.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "rabbitmq")
@Component
public class RabbitMQProperties {
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String exchange;
    private String queueIncidentCreated;
    private String queueStatusUpdated;
    private String routingKeyIncidentCreated;
    private String routingKeyStatusUpdated;
}
