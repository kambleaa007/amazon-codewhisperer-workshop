package com.cwworkshop.api;

import software.constructs.Construct;

import java.util.Map;

import com.cwworkshop.LambdaBundling;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.EventType;
import software.amazon.awscdk.services.s3.assets.AssetOptions;
import software.amazon.awscdk.services.s3.notifications.SnsDestination;
import software.amazon.awscdk.services.sns.Topic;
import software.amazon.awscdk.services.sns.subscriptions.SqsSubscription;
import software.amazon.awscdk.services.sqs.Queue;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.lambda.Code;

// java APIStack class that extends Stack class.
// This class defines the API stack.
// The API stack contains the following:
// - S3 bucket to store images
// - SNS topic to send notifications to
// - SQS queue to receive notifications from
// - Lambda function to get and save images
// - API gateway to expose the Lambda function
// - S3 bucket notification configuration to send notifications to the SNS topic
// - SNS topic subscription to the SQS queue
// - SQS queue policy to allow the Lambda function to read from the queue
// - SQS queue policy to allow the SNS topic to send notifications to the queue
// - SNS topic policy to allow the SNS topic to send notifications to the queue
// - SNS topic policy to allow the SQS queue to receive notifications from the topic
// - SQS queue policy to allow the SQS queue to receive notifications from the topic
// - SQS queue policy to allow the S3 bucket to read from the queue
// - S3 bucket policy to allow the Lambda function to read from the bucket
// - S3 bucket policy to allow the SQS queue to read from the bucket


public class APIStack extends Stack {

    private String uploadQueueUrl;
    private String uploadQueueArn;

    public String getUploadQueueUrl() {
        return this.uploadQueueUrl;
    }

    public String getUploadQueueArn() {
        return this.uploadQueueArn;
    }

    public APIStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public APIStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        final Bucket bucket = Bucket.Builder.create(this, "CW-Workshop-Images")
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();

        final Function imageGetAndSaveLambda = Function.Builder.create(this, "ImageGetAndSaveLambda")
                .functionName("ImageGetAndSaveLambda")
                .runtime(Runtime.JAVA_11)
                .memorySize(1024)
                .timeout(Duration.seconds(60))
                .code(Code.fromAsset(
                        "../software/api",
                        AssetOptions.builder().bundling(LambdaBundling.get("api")).build()))
                .handler("api.Handler")
                .environment(Map.of("BUCKET_NAME", bucket.getBucketName()))
                .build();

        bucket.grantReadWrite(imageGetAndSaveLambda);

        final RestApi api = RestApi.Builder.create(this, "REST_API")
                .restApiName("Image Upload Service")
                .description("CW workshop - upload image for workshop.")
                .build();

        final LambdaIntegration getImageIntegration = LambdaIntegration.Builder.create(imageGetAndSaveLambda)
                .requestTemplates(Map.of("application/json", "{ \"statusCode\": \"200\" }"))
                .build();

        api.getRoot().addMethod("GET", getImageIntegration);

        final Queue uploadQueue = Queue.Builder.create(this, "uploaded_image_queue")
                .visibilityTimeout(Duration.seconds(30))
                .build();

        this.uploadQueueUrl = uploadQueue.getQueueUrl();
        this.uploadQueueArn = uploadQueue.getQueueArn();

        final SqsSubscription sqsSubscription = SqsSubscription.Builder.create(uploadQueue)
                .rawMessageDelivery(true)
                .build();

        final Topic uploadEventTopic = Topic.Builder.create(this, "uploaded_image_topic")
                .build();

        uploadEventTopic.addSubscription(sqsSubscription);

        bucket.addEventNotification(
                EventType.OBJECT_CREATED_PUT,
                new SnsDestination(uploadEventTopic));
    }
}
