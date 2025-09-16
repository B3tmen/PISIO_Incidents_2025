package org.unibl.etf.incidentservice.controller;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.unibl.etf.incidentservice.model.dto.FileUploadResponse;
import org.unibl.etf.incidentservice.model.dto.IncidentDTO;
import org.unibl.etf.incidentservice.model.requests.IncidentRequest;
import org.unibl.etf.incidentservice.model.requests.TranslationRequest;
import org.unibl.etf.incidentservice.service.interfaces.AwsS3Service;
import org.unibl.etf.incidentservice.service.interfaces.IncidentService;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/incidents")
public class IncidentsController {
    private final IncidentService incidentService;
    private final AwsS3Service awsS3Service;

    @Autowired
    public IncidentsController(IncidentService incidentService, AwsS3Service awsS3Service) {
        this.incidentService = incidentService;
        this.awsS3Service = awsS3Service;
    }

    @GetMapping("/awsUrl")
    public ResponseEntity<?> getImageUrl(@RequestBody String key) {
        String url = awsS3Service.getPresignedUrl(key);
        return ResponseEntity.ok(url);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> insertIncident(
            @RequestPart("incidentRequest") IncidentRequest incidentRequest,
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) {
        IncidentDTO dto = incidentService.insertIncident(incidentRequest, images);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{incidentId}")
                .buildAndExpand(dto.getId())
                .toUri();

        return ResponseEntity.created(location).body(dto);
    }

    @GetMapping
    public ResponseEntity<?> getAllIncidents() {
        return ResponseEntity.ok(incidentService.getAllIncidents());
    }


    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        FileUploadResponse response = new FileUploadResponse();
        try {
            response = awsS3Service.uploadFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/translation")
    public ResponseEntity<?> translate(@RequestBody @Validated TranslationRequest translationRequest) {
        String translation = incidentService.translateText(translationRequest);
        return ResponseEntity.ok(translation);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> download(@PathVariable String fileName) throws IOException {
        System.out.println("Downloading file: " + fileName);
        byte[] data = awsS3Service.downloadFile(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .body(data);
    }
}
