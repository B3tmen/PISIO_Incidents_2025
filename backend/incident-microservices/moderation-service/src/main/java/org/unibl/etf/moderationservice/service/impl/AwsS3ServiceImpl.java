package org.unibl.etf.moderationservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.unibl.etf.moderationservice.repository.IncidentImageRepository;
import org.unibl.etf.moderationservice.service.interfaces.AwsS3Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;

@Service
public class AwsS3ServiceImpl implements AwsS3Service {
    @Value("${aws.bucket-name}")
    private String bucketName;

    private final S3Presigner presigner;


    @Autowired
    public AwsS3ServiceImpl(S3Presigner presigner) {
        this.presigner = presigner;
    }


    @Override
    public String getPresignedUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10)) // URL valid for 10 mins
                .getObjectRequest(getObjectRequest)
                .build();

        return presigner.presignGetObject(presignRequest).url().toString();
    }
}
