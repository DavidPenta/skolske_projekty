package com.musicverse.client.sessionManagement;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

public class MyLogger {
    private boolean fileExists;
    public MyLogger(String message,String type) {

        DocumentBuilder docBuilder = null;
        this.fileExists = false;
        try {
            docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Document doc = docBuilder.newDocument();

        LocalDateTime now = LocalDateTime.now();

        Element log = doc.createElement("Log");
        log.setAttribute("type",type);
        log.setAttribute("time", String.valueOf(now));
        log.setAttribute("message",message);
        doc.appendChild(log);

        try{
            File f = new File("myownlog.xml");
            if(f.exists()) {
                fileExists = true;
            }
        }
        catch (Exception e){

        }

        try (FileOutputStream output = new FileOutputStream("myownlog.xml",true)) {
            writeXml(doc, output);
        } catch (IOException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private void writeXml(Document doc, OutputStream output) throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        if(this.fileExists)
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);
        transformer.transform(source, result);

    }
}
