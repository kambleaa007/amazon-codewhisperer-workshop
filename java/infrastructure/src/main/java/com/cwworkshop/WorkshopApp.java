package com.cwworkshop;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import com.cwworkshop.api.APIStack;
import com.cwworkshop.integration.IntegrationStack;
import com.cwworkshop.recognition.RecognitionStack;
import com.cwworkshop.recognition.RecognitionStackProps;


// java WorkshopApp class that contains a main method. This class is used to create a CDK app and add the APIStack, IntegrationStack, and RecognitionStack to it.
// The APIStack and IntegrationStack are used to create the API and SNS integration, respectively.
// The RecognitionStack is used to create the Rekognition Lambda function.
// The main method is used to create the CDK app and add the APIStack, IntegrationStack, and RecognitionStack to it.
// The CDK app is then synthesized and deployed to AWS.
// The synthesized CDK app can then be used to test the Rekognition Lambda function.


public class WorkshopApp {
    private static final String DEFAULT_REGION = "us-east-2";

    public static void main(final String[] args) {
        App app = new App();

        Environment defaultEnv = Environment.builder().region(DEFAULT_REGION).build();
        StackProps defaultProps = StackProps.builder().env(defaultEnv).build();

        APIStack apiStack = new APIStack(app, "APIStack", defaultProps);
        IntegrationStack integrationStack = new IntegrationStack(app, "IntegrationStack", defaultProps);

        RecognitionStackProps recognitionStackProps = new RecognitionStackProps()
                .sqsArn(apiStack.getUploadQueueArn())
                .sqsUrl(apiStack.getUploadQueueUrl())
                .snsArn(integrationStack.getSnsArn())
                .withEnv(defaultEnv);

        new RecognitionStack(app, "RekognitionStack", recognitionStackProps);

        app.synth();
    }
}
