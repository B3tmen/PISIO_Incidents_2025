package org.unibl.etf.incidentservice.service.interfaces;

import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.incidentservice.model.dto.IncidentDTO;
import org.unibl.etf.incidentservice.model.requests.IncidentRequest;
import org.unibl.etf.incidentservice.model.requests.IncidentStatusUpdateRequest;
import org.unibl.etf.incidentservice.model.requests.TranslationRequest;

import java.util.List;

public interface IncidentService {
    IncidentDTO insertIncident(IncidentRequest incidentRequest, MultipartFile[] images);
    IncidentDTO getById(int id);
    List<IncidentDTO> getAllIncidents();
    String translateText(TranslationRequest translationRequest);
}
