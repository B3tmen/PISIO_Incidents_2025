package org.unibl.etf.moderationservice.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentImageDTO {
    private Long id;
    private String imageURL;
}