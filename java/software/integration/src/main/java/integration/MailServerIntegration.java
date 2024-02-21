package integration;

import java.net.URL;
import java.net.URLConnection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class MailServerIntegration {

    // 1.) Convert JSON string to XML string 
    public static String convertJsonToXml(String mail) throws Exception {
        ObjectMapper mapper = new XmlMapper();
        String xmlString = mapper.writeValueAsString(mail);
        return xmlString;
    }

    // 2.) Send XML string with HTTP POST
    public static void postXmlToMailServer(String xmlString) throws Exception {
        URL url = new URL("XXXXXXXXXXXXXXXXXXXXXXXXXX");
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        connection.getOutputStream().write(xmlString.getBytes());
    }

}