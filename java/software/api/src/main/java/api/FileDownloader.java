package api;

import java.net.URL;
import java.net.URLConnection;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class FileDownloader { 
    private static final String BUCKET_NAME = "XXXXXXXXXXXXXX";
    private static final String FILE_NAME = "my-file-name.txt";
    private static final String FILE_URL = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";


    // 1.) Function to download a file from URL
    public static void downloadFile(String url, String name) {
        try {
            URL fileUrl = new URL(url);
            URLConnection connection = fileUrl.openConnection();
            connection.connect();
            // <<Amazon CodeWhisperer generated code goes here>>
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 2.) Function to upload image to S3 bucket
    public static void uploadFileToS3() {
        AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
        s3Client.putObject(BUCKET_NAME, FILE_NAME, FILE_URL);
        System.out.println("File uploaded successfully!");
        // <<Amazon CodeWhisperer generated code goes here>>
    }
    
}
