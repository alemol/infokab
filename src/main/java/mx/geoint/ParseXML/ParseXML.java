package mx.geoint.ParseXML;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.xuggle.xuggler.Utils;
import mx.geoint.Model.GlosaStep;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
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

    /**
     *
     * @param annotation_ref es la referencia a la capa padre
     * @param steps ArrayList<GlosaStep> elementos de análisis de una anotación
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws IOException
     * @throws SAXException
     */
    public void writeElement(String annotation_ref, ArrayList<GlosaStep> steps) throws ParserConfigurationException, TransformerException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(new File(filePath));

        Element TIER = isTierInEaf(document, "Glosado");
        if(TIER == null){
            Element root = document.getDocumentElement();
            TIER = document.createElement("TIER");
            TIER.setAttribute("LINGUISTIC_TYPE_REF", "Glosado");
            root.appendChild(TIER);
        }

        Element ANNOTATION = document.createElement("ANNOTATION");
        for (GlosaStep step: steps){
            Element REF_ANNOTATION = document.createElement("REF_ANNOTATION");
            Element ANNOTATION_VALUE = document.createElement("ANNOTATION_VALUE");

            REF_ANNOTATION.setAttribute("ANNOTATION_ID", "g"+String.valueOf(annotation_ref));
            REF_ANNOTATION.setAttribute("ANNOTATION_WORD", String.valueOf(step.getId()));
            REF_ANNOTATION.setAttribute("ANNOTATION_REF", String.valueOf(annotation_ref));

            ANNOTATION_VALUE.appendChild(document.createTextNode(step.getSelect()));
            REF_ANNOTATION.appendChild(ANNOTATION_VALUE);
            ANNOTATION.appendChild(REF_ANNOTATION);
            TIER.appendChild(ANNOTATION);
        }

        saveFile(document);
    }

    /**
     *
     * @param doc Documento eaf parseado
     * @param LINGUISTIC_TYPE_REF tipo de referencia lingüística que caracteriza una capa de tier
     * @return Element en caso que exista la capa encontrada, de lo contrario null
     */
    private Element isTierInEaf(Document doc,  String LINGUISTIC_TYPE_REF){
        Element element = null;
        NodeList nodeList = doc.getDocumentElement().getElementsByTagName("TIER");
        for (int temp=0; temp<nodeList.getLength(); temp ++){
            Node tempNode = nodeList.item(temp);
            Element tempElement = (Element) tempNode;
            if(tempElement.getAttribute("LINGUISTIC_TYPE_REF").equals(LINGUISTIC_TYPE_REF)){
                element = tempElement;
                break;
            }
        }

        return element;
    }

    /**
     *
     * @param doc Documento eaf parseado
     * @throws TransformerException
     * @throws IOException
     */
    public void saveFile(Document doc) throws TransformerException, IOException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        DOMSource domSource = new DOMSource(doc);
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
