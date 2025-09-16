package org.unibl.etf.incidentservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDTO {
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal radius;
    private String address;
    private String city;
    private String country;
    private String zipCode;
}
