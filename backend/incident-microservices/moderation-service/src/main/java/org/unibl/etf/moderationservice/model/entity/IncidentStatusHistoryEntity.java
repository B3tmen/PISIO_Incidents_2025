package org.unibl.etf.moderationservice.model.entity;


import org.unibl.etf.moderationservice.model.enums.IncidentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "incident_status_history")
public class IncidentStatusHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Integer id;

    @Column(name = "incident_id")
    private Integer incidentId;

    @Enumerated(EnumType.STRING)
    private IncidentStatus status;

    @Column(name = "status_change_time")
    private LocalDateTime statusChangeTime;

    @Column(name = "moderator_id")
    private Integer moderatorId;
}