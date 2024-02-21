package com.cwworkshop;

import java.util.Arrays;
import java.util.List;

import software.amazon.awscdk.BundlingOptions;
import software.amazon.awscdk.DockerVolume;
import software.amazon.awscdk.services.lambda.Runtime;

import static java.util.Collections.singletonList;
import static software.amazon.awscdk.BundlingOutput.ARCHIVED;


// java LambdaBundling class that contains a static method get() that returns a BundlingOptions object.
// This method is used to create a BundlingOptions object that can be used to bundle the Lambda function.
// The BundlingOptions object contains the packaging instructions, the runtime, the volumes, the user, the output type, and the command.
// The packaging instructions are used to package the Lambda function into a JAR file.
// The runtime is used to specify the Java version to use for the Lambda function.
// The volumes are used to mount the local .m2 repo to the Lambda function container.
// The user is used to specify the user to run the packaging instructions as.
// The output type is used to specify the type of output to bundle the Lambda function into.
// The command is used to specify the packaging instructions to run.
// The get() method returns the BundlingOptions object.
// The LambdaBundling class is used to bundle the Lambda function into a JAR file.
// The get() method is used to create the BundlingOptions object.
// The BundlingOptions object is used to bundle the Lambda function.
// The LambdaBundling class is used to bundle the Lambda function into a JAR file.

public class LambdaBundling {
    public static BundlingOptions get(String packageName) {
        String cmd = "mvn clean install " +
                "&& cp /asset-input/target/%s.jar /asset-output/";

        List<String> packagingInstructions = Arrays.asList(
                "/bin/sh",
                "-c",
                String.format(cmd, packageName));

        BundlingOptions builderOptions = BundlingOptions.builder()
                .command(packagingInstructions)
                .image(Runtime.JAVA_11.getBundlingImage())
                .volumes(singletonList(
                        // Mount local .m2 repo to avoid download all the dependencies again inside the container
                        DockerVolume.builder()
                                .hostPath(System.getProperty("user.home") + "/.m2/")
                                .containerPath("/root/.m2/")
                                .build()))
                .user("root")
                .outputType(ARCHIVED)
                .build();

        return builderOptions;
    }
}
