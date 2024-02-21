package recognition;

import java.util.Map;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;

public class LabelStorage {
    private static final String TABLE_NAME = "Labels";
    private static final String TOPIC_ARN = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private static final String QUEUE_URL = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

    public static DynamoDbClient dynamoDbClient = DynamoDbClient.create();
    public static SnsClient snsClient = SnsClient.create();
    public static SqsClient sqsClient = SqsClient.create();

    public static PutItemResponse storeLabels(Map<String, AttributeValue> item) {
        
        PutItemResponse response = dynamoDbClient.putItem(PutItemRequest.builder().tableName(TABLE_NAME).item(item).build());
        return response;
        
        // 1.) Get message from SQS queue 
        // <<Amazon CodeWhisperer generated code goes here>>
            
        // 2.) Save json item to to DynamoDB  
            
        // <<Amazon CodeWhisperer generated code goes here>>

        // 3.) Publish item to SNS 
            
        // <<Amazon CodeWhisperer generated code goes here>>

        // 4.) Delete message from SQS 
            
        // <<Amazon CodeWhisperer generated code goes here>>
    }
    
    public static void saveJsonToDynamoDB(String jsonItem, String tableName){
        System.out.println("saveJsonToDynamoDB");
        dynamoDbClient.putItem(PutItemRequest.builder().tableName(tableName).build());
        System.out.println("saveJsonToDynamoDB");
    }
    
    public static void sendNotification(String[] labels, String topicArn){
        System.out.println("sendNotification");
        String message = "The following labels have been detected: " + labels;
        PublishRequest publishRequest = PublishRequest.builder().topicArn(topicArn).message(message).build();
        PublishResponse publishResponse = snsClient.publish(publishRequest);
        System.out.println(publishResponse.messageId() + " Message sent. Status is " + publishResponse.sdkHttpResponse().statusCode());
        System.out.println("sendNotification");
    }



    public static void deleteMessage(String receiptHandle, String queueUrl){
        System.out.println("deleteMessage");
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder().queueUrl(queueUrl).receiptHandle(receiptHandle).build();
        sqsClient.deleteMessage(deleteMessageRequest);
        System.out.println("deleteMessage");
    }
     
}
