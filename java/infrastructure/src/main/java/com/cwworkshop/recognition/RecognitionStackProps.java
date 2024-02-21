package com.cwworkshop.recognition;

import org.jetbrains.annotations.Nullable;

import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;



// java RecognitionStackProps class that implements StackProps interface. 
// This class is used to pass the environment variables to the CDK stack. 
// The CDK stack will use these environment variables to configure the SQS queue, SNS topic, and Lambda function. 
// The CDK stack will also use these environment variables to configure the Lambda function's IAM role. 
// The CDK stack will also use these environment variables to configure the Lambda function's environment variables. 
// The CDK stack will also use these environment variables to configure the Lambda function's VPC configuration. 
// The CDK stack will also use these environment variables to configure the Lambda function's security group. 
// The CDK stack will also use these environment variables to configure the Lambda function's timeout. 
// The CDK stack will also use these environment variables to configure the Lambda function's memory size. 
// The CDK stack will also use these environment variables to configure the Lambda function's runtime. 
// The CDK stack will also use these environment variables to configure the Lambda function's handler. 
// The CDK stack will also use these environment variables to configure the Lambda function's code. 

public class RecognitionStackProps implements StackProps {

    private String sqsUrl;
    private String sqsArn;
    private String snsArn;
    private Environment env;

    @Override
    public @Nullable Environment getEnv() {
        return this.env;
    }

    public String getSqsUrl() {
        return this.sqsUrl;
    }

    public String getSqsArn() {
        return this.sqsArn;
    }

    public String getSnsArn() {
        return this.snsArn;
    }

    public RecognitionStackProps sqsUrl(String sqsUrl) {
        this.sqsUrl = sqsUrl;
        return this;
    }

    public RecognitionStackProps sqsArn(String sqsArn) {
        this.sqsArn = sqsArn;
        return this;
    }

    public RecognitionStackProps snsArn(String snsArn) {
        this.snsArn = snsArn;
        return this;
    }

    public RecognitionStackProps withEnv(Environment env) {
        this.env = env;
        return this;
    }
}
