package org.unibl.etf.incidentservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.unibl.etf.incidentservice.model.dto.IncidentImageDTO;
import org.unibl.etf.incidentservice.model.dto.LocationDTO;
import org.unibl.etf.incidentservice.model.enums.IncidentStatus;
import org.unibl.etf.incidentservice.model.enums.IncidentSubtype;
import org.unibl.etf.incidentservice.model.enums.IncidentType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

//    @ColumnDefault("'REPORTED'")
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private IncidentStatus status;

    @Column(name = "reported_at", nullable = false)
    private LocalDateTime reportedAt;

    @Lob
    @Column(name = "description", length = 1000, columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_location_id", nullable = false)
    private LocationEntity location;

    @OneToMany(mappedBy = "incident", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<IncidentImageEntity> images = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        reportedAt = LocalDateTime.now();
        status = IncidentStatus.REPORTED;
    }

    public void addImage(IncidentImageEntity image) {
        images.add(image);
        image.setIncident(this);
    }
}
