package recognition;

import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;

public class ImageRecognizer {

    // 1.) Function to detect labels from image with Rekognition as "labels" 
    public static String[] detectLabels(String bucketName, String imageName) {
        RekognitionClient rekognitionClient = RekognitionClient.create();

        DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest.builder()
                .image(Image.builder().s3Object(s -> s.bucket(bucketName).name(imageName)).build())
                .maxLabels(10)
                .build();

        DetectLabelsResponse detectLabelsResponse = rekognitionClient.detectLabels(detectLabelsRequest);

        String[] labels = new String[detectLabelsResponse.labels().size()];
        for (int i = 0; i < detectLabelsResponse.labels().size(); i++) {
            labels[i] = detectLabelsResponse.labels().get(i).name();
        }

        return labels;
    }
}
