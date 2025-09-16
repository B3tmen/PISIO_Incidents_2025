package org.unibl.etf.incidentservice.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.unibl.etf.incidentservice.model.dto.IncidentImageDTO;
import org.unibl.etf.incidentservice.model.dto.LocationDTO;
import org.unibl.etf.incidentservice.model.enums.IncidentSubtype;
import org.unibl.etf.incidentservice.model.enums.IncidentType;

@Data
@AllArgsConstructor
public class IncidentRequest {
    private IncidentType type;
    private IncidentSubtype subtype;
    private String description;
    private LocationDTO location;
}
