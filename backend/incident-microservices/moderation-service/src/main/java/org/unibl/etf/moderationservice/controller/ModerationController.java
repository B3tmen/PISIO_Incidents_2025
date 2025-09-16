package org.unibl.etf.moderationservice.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.moderationservice.model.dto.IncidentDTO;
import org.unibl.etf.moderationservice.model.dto.IncidentModerationDTO;
import org.unibl.etf.moderationservice.model.enums.IncidentStatus;
import org.unibl.etf.moderationservice.model.request.IncidentStatusUpdateRequest;
import org.unibl.etf.moderationservice.service.interfaces.ModerationService;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/v1/moderation")
public class ModerationController {

    private final ModerationService moderationService;

    public ModerationController(ModerationService moderationService) {
        this.moderationService = moderationService;
    }

    @GetMapping("/incidents")
    public ResponseEntity<?> getPendingIncidents() {
        List<IncidentModerationDTO> incidents = moderationService.getAllIncidents();
        return ResponseEntity.ok().body(incidents);
    }

    @GetMapping("/{incidentId}")
    public ResponseEntity<IncidentStatus> getIncidentStatus(@PathVariable Integer incidentId) {
        return ResponseEntity.ok(moderationService.getIncidentStatus(incidentId));
    }

    @PutMapping("/{incidentId}/status")
    public ResponseEntity<IncidentModerationDTO> updateIncidentStatus(@PathVariable int incidentId, @RequestBody IncidentStatusUpdateRequest request) {
        // TODO: Pass moderatorId, after authentication part
        return ResponseEntity.ok(moderationService.updateIncidentStatus(incidentId, request.getStatus(), 1));
    }

    @GetMapping("/incidents/{incidentId}")
    public ResponseEntity<IncidentDTO> getIncident(@PathVariable Integer incidentId) {
        return ResponseEntity.ok(moderationService.getIncident(incidentId));
    }
}
