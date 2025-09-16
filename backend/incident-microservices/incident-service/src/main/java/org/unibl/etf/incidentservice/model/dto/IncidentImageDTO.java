package org.unibl.etf.incidentservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentImageDTO {
    private Integer id;
    private String imageURL;
}
