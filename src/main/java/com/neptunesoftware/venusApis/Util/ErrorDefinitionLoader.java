package com.neptunesoftware.venusApis.Util;

import com.neptunesoftware.supernova.ws.client.security.BasicHTTPAuthenticator;
import com.neptunesoftware.supernova.ws.server.account.AccountWebServiceStub;
import com.neptunesoftware.supernova.ws.server.customer.CustomerWebServiceStub;
import com.neptunesoftware.supernova.ws.server.security.SecurityWebServiceStub;
import com.neptunesoftware.supernova.ws.server.transaction.TransactionsWebServiceStub;
import com.neptunesoftware.supernova.ws.server.transfer.FundsTransferWebServiceStub;
import com.neptunesoftware.supernova.ws.server.txnprocess.TxnProcessWebServiceStub;
import com.neptunesoftware.venusApis.Beans.AppProps;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.net.Authenticator;

@Component
@RequiredArgsConstructor
public class ErrorDefinitionLoader implements CommandLineRunner {
    public static NodeList errorList;
    private final AppProps appProps;


    @Override
    public void run(String... args) {
        try {
            loadXapiCodes();
        } catch (Exception e) {
            Logging.error(e);
        }
    }

    private void loadXapiCodes() {

        File xmlFile = new File(appProps.xapiFilePath + "xapicodes.xml");
        if (!xmlFile.exists()) {
            System.out.println("XAPI Codes XML file not found");
        }
        try (FileInputStream fis = new FileInputStream(xmlFile)) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            factory.setFeature("http://xml.org/sax/features/validation", false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(fis);
            doc.getDocumentElement().normalize();

            errorList = doc.getElementsByTagName("entry");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
