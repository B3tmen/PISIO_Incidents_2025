package org.unibl.etf.moderationservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.moderationservice.model.enums.IncidentStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "incident_moderation")
public class IncidentModerationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "incident_moderation_id")
    private Integer id;

    @Column(name = "incident_id", nullable = false)
    private Integer incidentId;

    @Enumerated(EnumType.STRING)
    private IncidentStatus status;

    @Column(name = "status_change_time")
    private LocalDateTime statusChangeTime;

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IncidentImageEntity> images = new ArrayList<>();

    @Column(name = "moderator_id")
    private Integer moderatorId = null;

    public void addImage(IncidentImageEntity image) {
        this.images.add(image);
        image.setIncident(this);
    }
}