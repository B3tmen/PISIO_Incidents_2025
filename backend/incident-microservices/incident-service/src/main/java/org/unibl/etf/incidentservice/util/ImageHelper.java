package org.unibl.etf.incidentservice.util;

import org.unibl.etf.incidentservice.model.entity.IncidentEntity;
import org.unibl.etf.incidentservice.service.interfaces.AwsS3Service;

import java.util.List;

public class ImageHelper {
    public static void setupImageURLs(List<IncidentEntity> incidentEntities, AwsS3Service awsS3Service) {
        incidentEntities.stream()
                .flatMap(entity -> entity.getImages().stream())
                .forEach(image ->
                        image.setImageURL(awsS3Service.getPresignedUrl(image.getImageURL()))
                );
    }
}
