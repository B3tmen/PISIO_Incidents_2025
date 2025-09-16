package org.unibl.etf.analyticsservice.model.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.analyticsservice.util.Constants;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {
    private Integer id;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal radius;
    private String address = Constants.UNKNOWN_FILLER;
    private String city = Constants.UNKNOWN_FILLER;
    private String state = Constants.UNKNOWN_FILLER;
    private String country = Constants.UNKNOWN_FILLER;
    private String zipCode = Constants.UNKNOWN_FILLER;
}
