package mx.geoint.ParseXML;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class ParseXML {
    String filePath = "";
    String name = "";
    ParseHandler parseHandler;

    /**
     * Inicializa la instancia con el path y la clase ParseHandler
     * @param path String, Ruta del archivo eaf
     * @param tier_id String, Identificador de tier a obtener en el archivo eaf
     */
    public ParseXML(String path, String tier_id) {
        String normalize = Normalizer.normalize(tier_id.toLowerCase(), Normalizer.Form.NFD);
        String new_tier_id = normalize.replaceAll("[^\\p{ASCII}]", "");
        filePath = path;
        parseHandler = new ParseHandler(new_tier_id);
    }

    /**
     * Leer el archivo .eaf y obtiene por medio el metodo de SAXParser el tier_id con sus tiempos
     */
    public void read() {
        try{
            File inputFile = new File(filePath);
            name = inputFile.getName();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(inputFile, parseHandler);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setElement() throws ParserConfigurationException, TransformerException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(new File(filePath));

        Element root = document.getDocumentElement();
        Element TIER = document.createElement("TIER");

        TIER.setAttribute("LINGUISTIC_TYPE_REF", "Glosado");

        root.appendChild(TIER);

        for (int i = 1; i <= 3; i++) {
            Element ANNOTATION = document.createElement("ANNOTATION");
            Element REF_ANNOTATION = document.createElement("REF_ANNOTATION");
            Element ANNOTATION_VALUE = document.createElement("ANNOTATION_VALUE");

            REF_ANNOTATION.setAttribute("ANNOTATION_ID", String.valueOf(i));
            REF_ANNOTATION.setAttribute("ANNOTATION_REF", String.valueOf(i));

            ANNOTATION_VALUE.appendChild(document.createTextNode("Anotación"));
            REF_ANNOTATION.appendChild(ANNOTATION_VALUE);
            ANNOTATION.appendChild(REF_ANNOTATION);
            TIER.appendChild(ANNOTATION);
        }

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new StringWriter());
        transformer.transform(domSource, streamResult);

        String xmlStr = streamResult.getWriter().toString().replaceAll("\\s+\n", "\n");
        Files.writeString(Path.of(filePath), xmlStr, StandardCharsets.UTF_8);
    }

    /**
     * Regresar una lista de la clase tier que se obtuvo de parse del archivo .eaf
     * @return List<Tier> Lista de instancias de la clase Tier
     */
    public List<Tier> getTier(){
        return parseHandler.getTier();
    }

    /**
     * Regresa el mimeType del archivo multimedia obtenido del archivo eaf
     * @return String, mimeType del archivo multimedia en el archivo eaf
     */
    public String getMimeType(){
        return parseHandler.getMimeType();
    }
}
