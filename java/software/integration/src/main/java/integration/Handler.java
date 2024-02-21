package integration;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Handler implements RequestHandler<SQSEvent, String> {

    public String handleRequest(SQSEvent event, Context context) {

        try {
            final var body = new ObjectMapper().writeValueAsString(event);

            // Call MailServerIntegration class with var "body" to convert json to xml 
            final var xml = MailServerIntegration.convertJsonToXml(body);

            System.out.println(xml);

            // <<Amazon CodeWhisperer generated code goes here>>

            // Call MailServerIntegration class to post xml to mail server
            MailServerIntegration.postXmlToMailServer(xml);

            System.out.println("Mail sent!");

            System.out.println("Body: " + body);
            System.out.println("XML: " + xml);

            System.out.println("Event: " + event);
            System.out.println("Context: " + context);

            System.out.println("Event Records: " + event.getRecords());
            System.out.println("Event Records 0: " + event.getRecords().get(0));
            System.out.println("Event Records 0 Body: " + event.getRecords().get(0).getBody());
            System.out.println("Event Records 0 Message Attributes: " + event.getRecords().get(0).getMessageAttributes());
            System.out.println("Event Records 0 Message Attributes S3Bucket: " + event.getRecords().get(0).getMessageAttributes().get("S3Bucket"));
            
            // <<Amazon CodeWhisperer generated code goes here>>
            
            return "200 OK";
        } catch (Exception e) {
            e.printStackTrace();
            return "500";
        }
    }
}
