package org.unibl.etf.moderationservice.service.interfaces;

public interface AwsS3Service {
    String getPresignedUrl(String key);
}
