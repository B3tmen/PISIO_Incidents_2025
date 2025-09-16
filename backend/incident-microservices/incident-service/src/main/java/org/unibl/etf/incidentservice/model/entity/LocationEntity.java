package org.unibl.etf.incidentservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.incidentservice.util.Constants;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "location")
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id", nullable = false)
    private Integer id;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal radius;

    @Column(length = 45, nullable = false)
    private String address = Constants.UNKNOWN_FILLER;

    @Column(length = 45, nullable = false)
    private String city = Constants.UNKNOWN_FILLER;

    @Column(length = 45, nullable = false)
    private String state = Constants.UNKNOWN_FILLER;

    @Column(length = 45, nullable = false)
    private String country = Constants.UNKNOWN_FILLER;

    @Column(name = "zip_code", length = 10, nullable = false)
    private String zipCode = Constants.UNKNOWN_FILLER;
}
