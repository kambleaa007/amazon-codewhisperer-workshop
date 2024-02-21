package com.cwworkshop.integration;

import com.cwworkshop.LambdaBundling;

import software.constructs.Construct;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.sns.Topic;
import software.amazon.awscdk.services.sns.subscriptions.SqsSubscription;
import software.amazon.awscdk.services.sqs.Queue;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.eventsources.SqsEventSource;
import software.amazon.awscdk.services.s3.assets.AssetOptions;
import software.amazon.awscdk.services.lambda.Code;


// java IntegrationStack class that extends Stack class.
// This class defines the integration stack.
// The integration stack contains the following:
// - SQS queue to receive notifications from
// - SNS topic to send notifications to
// - Lambda function to process the notifications
// - SQS queue policy to allow the Lambda function to read from the queue
// - SNS topic policy to allow the SNS topic to send notifications to the queue
// - SNS topic subscription to the SQS queue
// - SQS queue policy to allow the SQS queue to receive notifications from the topic
// - SQS queue policy to allow the SNS topic to send notifications to the queue
// - SNS topic policy to allow the SQS queue to receive notifications from the topic
// - SQS queue policy to allow the SQS queue to receive notifications from the topic
// - SQS queue policy to allow the Lambda function to write to the queue
// - SNS topic policy to allow the Lambda function to write to the topic
// - SNS topic policy to allow the SQS queue to receive notifications from the topic
// - SQS queue policy to allow the SQS queue to receive notifications from the topic

public class IntegrationStack extends Stack {
    private String rekognizedEventTopicArn;

    public String getSnsArn() {
        return this.rekognizedEventTopicArn;
    }

    public IntegrationStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public IntegrationStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        final Queue rekognizedQueue = Queue.Builder.create(this, "rekognized_image_queue")
                .visibilityTimeout(Duration.seconds(30))
                .build();

        final SqsSubscription sqsSubscription = SqsSubscription.Builder.create(rekognizedQueue)
                .rawMessageDelivery(true)
                .build();

        final Topic rekognizedEventTopic = Topic.Builder.create(this, "rekognized_image_topic")
                .build();

        this.rekognizedEventTopicArn = rekognizedEventTopic.getTopicArn();
        rekognizedEventTopic.addSubscription(sqsSubscription);

        final Function integrationLambda = Function.Builder.create(this, "IntegrationLambda")
                .runtime(Runtime.JAVA_11)
                .timeout(Duration.seconds(60))
                .memorySize(1024)
                .handler("integration.Handler")
                .code(Code.fromAsset(
                    "../software/integration",
                    AssetOptions.builder().bundling(LambdaBundling.get("integration")).build()))
                .build();

        final SqsEventSource invokeEventSource = SqsEventSource.Builder.create(rekognizedQueue).build();
        integrationLambda.addEventSource(invokeEventSource);
    }
}
