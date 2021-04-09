package org.bahmni.openerp.web.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class OpenERPResponseErrorValidator {
    private static final Logger logger = LogManager.getLogger(OpenERPResponseErrorValidator.class);
    private static final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

    public void checkForError(String response) {
        StringBuilder errorMessage = new StringBuilder();
        try {
            Document document = getDocumentBuilder().parse(new ByteArrayInputStream(response.getBytes()));
            XPath xPath =  XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile("methodResponse/fault/descendant::member/value/string").evaluate(document, XPathConstants.NODESET);

            if(nodeList.getLength() == 0) {
                return;
            }

            for(int index = 0; index < nodeList.getLength(); index++) {
                Node item = nodeList.item(index);
                errorMessage.append(item.getTextContent());
                errorMessage.append("\n");
            }
            throw new RuntimeException(errorMessage.toString());
        } catch (XPathExpressionException | SAXException | IOException e) {
            logger.error("Error while parsing OpenERP response : " + response);
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    private DocumentBuilder getDocumentBuilder() {
        try {
            return builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
