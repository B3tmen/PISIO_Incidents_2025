package org.unibl.etf.moderationservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "image")
public class IncidentImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    private Integer id;

    @Column(name = "image_url")
    private String imageURL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_incident_moderation_id", nullable = false)
    private IncidentModerationEntity incident;
}
