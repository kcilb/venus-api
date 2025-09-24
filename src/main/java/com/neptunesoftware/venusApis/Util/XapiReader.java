package com.neptunesoftware.venusApis.Util;

import com.neptunesoftware.supernova.ws.server.transaction.data.TxnResponseOutputData;
import com.neptunesoftware.supernova.ws.server.transfer.data.FundsTransferOutputData;
import com.neptunesoftware.venusApis.Models.ApiResponse;
import com.neptunesoftware.venusApis.Models.Response;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class XapiReader {

    private static String extractXmlContent(String input) {
        Pattern pattern = Pattern.compile("(<errors>.*?</errors>)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static Response readXapi(String exceptionMessage, ApiResponse<String> response) {
        String errorMessage = "";
        String xmlContent = extractXmlContent(exceptionMessage);
        if (xmlContent != null) {
            try {
                errorMessage = parseXml(xmlContent);
                if (errorMessage.isBlank()) {
                    response.response = StaticRefs.serverError();
                } else {
                    response.response = StaticRefs.customMessage("-909", errorMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No XML content found!");
        }
        return response.getResponse();
    }

    public static void readXapi2(String exceptionMessage, ApiResponse<FundsTransferOutputData> apiResponse) {
        String errorMessage = "";
        String xmlContent = extractXmlContent(exceptionMessage);
        if (xmlContent != null) {
            try {
                errorMessage = parseXml(xmlContent);
                System.out.println("No XML content found!");
                if (errorMessage.isBlank())
                    apiResponse.setResponse(StaticRefs.serverError());
                else {
                    apiResponse.setResponse(StaticRefs.customMessage("-909", errorMessage));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No XML content found!");
        }
    }


    private static String parseXml(String xmlContent) throws ParserConfigurationException, IOException, SAXException {
        // Initialize the XML parser
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
        Document document = builder.parse(inputStream);

        // Normalize XML structure
        document.getDocumentElement().normalize();

        // Process elements
        NodeList errorNodes = document.getElementsByTagName("error");
        for (int i = 0; i < errorNodes.getLength(); i++) {
            Node errorNode = errorNodes.item(i);

            if (errorNode.getNodeType() == Node.ELEMENT_NODE) {
                Element errorElement = (Element) errorNode;
                String errorCode = errorElement.getElementsByTagName("error-code").item(0).getTextContent();
                String errorLevel = errorElement.getElementsByTagName("error-level").item(0).getTextContent();
                return extractDescription(errorCode);
            }
        }
        return "";
    }

    private static String extractDescription(String errorCode) {

        try {
            //get loaded XAPI codes
            NodeList entryList = ErrorDefinitionLoader.errorList;
            for (int i = 0; i < entryList.getLength(); i++) {
                Node entryNode = entryList.item(i);

                if (entryNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element entryElement = (Element) entryNode;

                    // Check the 'key' attribute
                    String currentKey = entryElement.getAttribute("key");
                    if (errorCode.equals("SEC_1005")) {
                        return "Authentication failed, logout from Orbit-R or Confirm Your Login ID and/or Password Are Correct.";
                    }
                    if (currentKey.equals(errorCode)) {
                        // Return the text content of the <entry>
                        return entryElement.getTextContent();
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
