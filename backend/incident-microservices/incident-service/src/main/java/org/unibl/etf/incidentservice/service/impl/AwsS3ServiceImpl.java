package org.unibl.etf.incidentservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.incidentservice.model.dto.FileUploadResponse;
import org.unibl.etf.incidentservice.model.dto.IncidentImageDTO;
import org.unibl.etf.incidentservice.repository.IncidentImageRepository;
import org.unibl.etf.incidentservice.service.interfaces.AwsS3Service;
import org.unibl.etf.incidentservice.util.Constants;
import org.unibl.etf.incidentservice.util.FileHelper;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AwsS3ServiceImpl implements AwsS3Service {
    @Value("${aws.bucket-name}")
    private String bucketName;

    private final S3Client s3Client;
    private final S3AsyncClient s3AsyncClient;
    private final S3Presigner presigner;


    @Autowired
    public AwsS3ServiceImpl(S3Client s3Client, S3AsyncClient s3AsyncClient, S3Presigner presigner) {
        this.s3Client = s3Client;
        this.s3AsyncClient = s3AsyncClient;
        this.presigner = presigner;
    }

    @Override
    public FileUploadResponse uploadFile(MultipartFile file) throws IOException {
        String uniqueName = Constants.Aws.S3_BUCKET_IMAGES_FOLDER_PREFIX + FileHelper.getUniqueFileName(file.getOriginalFilename());
        try (InputStream is = file.getInputStream()) {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(uniqueName)
                            .build(),
                    RequestBody.fromInputStream(is, file.getSize())
            );
        }

        return new FileUploadResponse(uniqueName, LocalDateTime.now());


//        String uniqueName = Constants.Aws.S3_BUCKET_IMAGES_FOLDER_PREFIX
//                + FileHelper.getUniqueFileName(file.getOriginalFilename());
//
//        CompletableFuture<Void> future = s3Client.putObject(
//                PutObjectRequest.builder()
//                        .bucket(bucketName)
//                        .key(uniqueName)
//                        .build(),
//                AsyncRequestBody.fromBytes(file.getBytes())
//        ).thenAccept(response -> {
//            // You can log response metadata here if needed
//        });
//
//        // Block until upload finishes for single-file upload
//        future.join();
//
//        return new FileUploadResponse(uniqueName, LocalDateTime.now());
    }

    @Override
    public List<IncidentImageDTO> uploadFiles(MultipartFile[] files) throws IOException {
        List<IncidentImageDTO> incidentImageDTOs = new ArrayList<>();
        for (MultipartFile file : files) {
            String uniqueName = Constants.Aws.S3_BUCKET_IMAGES_FOLDER_PREFIX + FileHelper.getUniqueFileName(file.getOriginalFilename());

            try (InputStream inputStream = file.getInputStream()) {
                s3Client.putObject(
                        PutObjectRequest.builder()
                                .bucket(bucketName)
                                .key(uniqueName)
                                .build(),
                        RequestBody.fromInputStream(inputStream, file.getSize())
                );
            }

            incidentImageDTOs.add(new IncidentImageDTO(null, uniqueName));
        }

        return incidentImageDTOs;

//        if(files.length == 1) {
//            FileUploadResponse resp = uploadFile(files[0]);
//            return List.of(new IncidentImageDTO(null, resp.getFilePath(), null));
//        }
//
//        List<CompletableFuture<IncidentImageDTO>> futures = new ArrayList<>();
//
//        for (MultipartFile file : files) {
//            String uniqueName = Constants.Aws.S3_BUCKET_IMAGES_FOLDER_PREFIX
//                    + FileHelper.getUniqueFileName(file.getOriginalFilename());
//
//            CompletableFuture<IncidentImageDTO> uploadFuture = s3AsyncClient.putObject(
//                    PutObjectRequest.builder()
//                            .bucket(bucketName)
//                            .key(uniqueName)
//                            .build(),
//                    AsyncRequestBody.fromBytes(file.getBytes())
//            ).thenApply(response -> new IncidentImageDTO(null, uniqueName, null));
//
//            futures.add(uploadFuture);
//        }
//
//        // Wait for all uploads concurrently and collect results
//        return futures.stream()
//                .map(CompletableFuture::join)
//                .collect(Collectors.toList());
    }

    @Override
    public byte[] downloadFile(String key) throws IOException {
//        ResponseBytes<GetObjectResponse> objectAsBytes = s3Client.getObjectAsBytes(GetObjectRequest.builder()
//                .bucket(bucketName)
//                .key(Constants.Aws.S3_BUCKET_IMAGES_FOLDER_PREFIX + key)
//                .build()
//        );
//
//        return objectAsBytes.asByteArray();


        CompletableFuture<ResponseBytes<GetObjectResponse>> future = s3AsyncClient.getObject(
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(Constants.Aws.S3_BUCKET_IMAGES_FOLDER_PREFIX + key)
                        .build(),
                AsyncResponseTransformer.toBytes()
        );

        return future.join().asByteArray(); // block to return synchronously
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
