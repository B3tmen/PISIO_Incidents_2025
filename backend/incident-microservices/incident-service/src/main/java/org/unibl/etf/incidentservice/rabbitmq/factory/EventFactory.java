package org.unibl.etf.incidentservice.rabbitmq.factory;

import org.unibl.etf.incidentservice.model.entity.IncidentEntity;
import org.unibl.etf.incidentservice.rabbitmq.events.IncidentAnalyticsCreatedEvent;
import org.unibl.etf.incidentservice.rabbitmq.events.IncidentCreatedEvent;

public interface EventFactory {
    IncidentCreatedEvent createIncidentCreatedEvent(IncidentEntity incidentEntity);
    IncidentAnalyticsCreatedEvent createIncidentAnalyticsCreatedEvent(IncidentEntity incidentEntity);
}
