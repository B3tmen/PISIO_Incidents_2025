package org.unibl.etf.analyticsservice.model.entity;

import jakarta.persistence.*;
import jakarta.ws.rs.DefaultValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.unibl.etf.analyticsservice.model.enums.IncidentSubtype;
import org.unibl.etf.analyticsservice.model.enums.IncidentType;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "incident")
public class IncidentEntity {
    @Id
    @Column(name = "incident_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private IncidentType type;

    @Column(name = "subtype")
    @Enumerated(EnumType.STRING)
    private IncidentSubtype subtype;

    @Column(name = "reported_at", nullable = false)
    private LocalDateTime reportedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_location_id", nullable = false)
    private LocationEntity location;

    @PrePersist
    protected void onCreate() {
        reportedAt = LocalDateTime.now();
    }

}

