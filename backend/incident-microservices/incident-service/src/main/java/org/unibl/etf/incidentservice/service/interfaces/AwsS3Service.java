package org.unibl.etf.incidentservice.service.interfaces;

import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.incidentservice.model.dto.FileUploadResponse;
import org.unibl.etf.incidentservice.model.dto.IncidentImageDTO;

import java.io.IOException;
import java.util.List;

public interface AwsS3Service {
    FileUploadResponse uploadFile(MultipartFile file) throws IOException;
    List<IncidentImageDTO> uploadFiles(MultipartFile[] files) throws IOException;
    byte[] downloadFile(String key) throws IOException;
    String getPresignedUrl(String key);
}
