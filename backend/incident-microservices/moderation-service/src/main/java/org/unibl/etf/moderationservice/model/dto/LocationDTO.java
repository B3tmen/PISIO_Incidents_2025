package org.unibl.etf.moderationservice.model.dto;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDTO {

    private double latitude;
    private double longitude;
    private double radius;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipcode;
}

