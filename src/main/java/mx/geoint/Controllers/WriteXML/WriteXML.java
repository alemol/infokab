package mx.geoint.Controllers.WriteXML;

import com.google.gson.Gson;
import mx.geoint.Controllers.ParseXML.ParseHandler;
import mx.geoint.Model.Annotation.AnnotationRegister;
import mx.geoint.Model.Glosado.GlosaStep;
import mx.geoint.pathSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.ArrayList;

public class WriteXML {
    String filePath = "";
    public WriteXML(String path) {
        filePath = path;
    }

    public void writeElement(String annotation_ref, String annotation_tier_ref, ArrayList<GlosaStep> steps) throws ParserConfigurationException, TransformerException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(new File(filePath));

        Element TIER = isTierInEaf(document, pathSystem.TIER_GlOSA);
        if(TIER == null){
            Element root = document.getDocumentElement();
            TIER = document.createElement("TIER");
            TIER.setAttribute("LINGUISTIC_TYPE_REF", pathSystem.TIER_GlOSA);
            root.appendChild(TIER);
        }else{
            removeAnnotationinTier(TIER, String.valueOf(annotation_tier_ref));
        }

        Element ANNOTATION = document.createElement("ANNOTATION");
        Element REF_ANNOTATION = document.createElement("REF_ANNOTATION");

        ANNOTATION.setAttribute("ANNOTATION_TIER_REF", String.valueOf(annotation_tier_ref));
        REF_ANNOTATION.setAttribute("ANNOTATION_ID", "g"+String.valueOf(annotation_ref));
        REF_ANNOTATION.setAttribute("ANNOTATION_TIER_REF", String.valueOf(annotation_tier_ref));
        REF_ANNOTATION.setAttribute("ANNOTATION_REF", String.valueOf(annotation_ref));

        for (GlosaStep step: steps){
            Element ANNOTATION_VALUE = document.createElement("ANNOTATION_VALUE");
            ANNOTATION_VALUE.setAttribute("WORD_POSITION", String.valueOf(step.getId()));
            ANNOTATION_VALUE.setAttribute("ANNOTATION_WORD", String.valueOf(step.getWord()));
            ANNOTATION_VALUE.appendChild(document.createTextNode(step.getSelect()));

            REF_ANNOTATION.appendChild(ANNOTATION_VALUE);
        }

        ANNOTATION.appendChild(REF_ANNOTATION);
        TIER.appendChild(ANNOTATION);

        saveFile(document);
    }

    public void writeAnnotationRegisters(ArrayList<AnnotationRegister> annotationRegisters) throws ParserConfigurationException, TransformerException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(new File(filePath));

        removeTier(document, pathSystem.TIER_GlOSA);
        Element root = document.getDocumentElement();
        Element TIER = document.createElement("TIER");
        TIER.setAttribute("LINGUISTIC_TYPE_REF", pathSystem.TIER_GlOSA);
        root.appendChild(TIER);

        for(AnnotationRegister annotationRegister: annotationRegisters){
            Element ANNOTATION = document.createElement("ANNOTATION");
            Element REF_ANNOTATION = document.createElement("REF_ANNOTATION");

            ANNOTATION.setAttribute("ANNOTATION_TIER_REF", annotationRegister.getANNOTATION_TIER_REF());
            REF_ANNOTATION.setAttribute("ANNOTATION_ID", annotationRegister.getANNOTATION_ID());
            REF_ANNOTATION.setAttribute("ANNOTATION_TIER_REF", annotationRegister.getANNOTATION_TIER_REF());
            REF_ANNOTATION.setAttribute("ANNOTATION_REF", annotationRegister.getANNOTATION_REF());

            ArrayList<GlosaStep> glosaSteps = annotationRegister.getSteps();

            for (GlosaStep step: glosaSteps){
                Element ANNOTATION_VALUE = document.createElement("ANNOTATION_VALUE");
                ANNOTATION_VALUE.setAttribute("WORD_POSITION", String.valueOf(step.getId()));
                ANNOTATION_VALUE.setAttribute("ANNOTATION_WORD", String.valueOf(step.getWord()));
                ANNOTATION_VALUE.appendChild(document.createTextNode(step.getSelect()));

                REF_ANNOTATION.appendChild(ANNOTATION_VALUE);
            }

            ANNOTATION.appendChild(REF_ANNOTATION);
            TIER.appendChild(ANNOTATION);
        }

        saveFile(document);
    }

    /**
     * Elimina el nodo de la capa de glosado con el anotacion de referenci
     */
    private void removeTier(Document doc,  String LINGUISTIC_TYPE_REF){
        Element element = null;
        NodeList nodeList = doc.getDocumentElement().getElementsByTagName("TIER");
        for (int temp=0; temp<nodeList.getLength(); temp ++){
            Node tempNode = nodeList.item(temp);
            Element tempElement = (Element) tempNode;

            String NORMALIZE_LINGUISTIC_TYPE_REF = Normalizer.normalize(tempElement.getAttribute("LINGUISTIC_TYPE_REF").toLowerCase(), Normalizer.Form.NFD);
            String TAG_LINGUISTIC_TYPE_REF = NORMALIZE_LINGUISTIC_TYPE_REF.replaceAll("[^\\p{ASCII}]", "");
            if(TAG_LINGUISTIC_TYPE_REF.equals(LINGUISTIC_TYPE_REF)){
                element = tempElement;
                doc.getDocumentElement().removeChild(tempNode);
                break;
            }
        }
    }

    /**
     * Elimina el nodo de la capa de glosado con el anotacion de referencia
     * @param tier Nodo de la capa tier de glosado
     * @param ANNOTATION_TIER_REF identificador de la anotacion
     * @return elemento o nodo eliminado
     */
    private Element removeAnnotationinTier(Element tier,  String ANNOTATION_TIER_REF){
        Element element = null;
        NodeList nodeList = tier.getElementsByTagName("ANNOTATION");
        for (int temp=0; temp<nodeList.getLength(); temp ++){
            Node tempNode = nodeList.item(temp);
            Element tempElement = (Element) tempNode;
            if(tempElement.getAttribute("ANNOTATION_TIER_REF").equals(ANNOTATION_TIER_REF)){
                element = tempElement;
                tier.removeChild(tempNode);
                break;
            }
        }

        return element;
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

            String NORMALIZE_LINGUISTIC_TYPE_REF = Normalizer.normalize(tempElement.getAttribute("LINGUISTIC_TYPE_REF").toLowerCase(), Normalizer.Form.NFD);
            String TAG_LINGUISTIC_TYPE_REF = NORMALIZE_LINGUISTIC_TYPE_REF.replaceAll("[^\\p{ASCII}]", "");
            if(TAG_LINGUISTIC_TYPE_REF.equals(LINGUISTIC_TYPE_REF)){
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

    public boolean editAnnotation(String annotation_ref, String annotation, String tierName) throws ParserConfigurationException, TransformerException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(new File(filePath));

        Element TIER = isTierInEaf(document, tierName);
        if(TIER == null){ return false; }

        changeAnnotationinTier(document, TIER, annotation_ref, annotation);
        saveFile(document);
        return true;
    }

    private void changeAnnotationinTier(Document doc, Element tier,  String ANNOTATION_TIER_REF, String annotation){
        NodeList nodeListREF_ANNOTATION = tier.getElementsByTagName("REF_ANNOTATION");
        NodeList nodeListALIGNABLE_ANNOTATION = tier.getElementsByTagName("ALIGNABLE_ANNOTATION");

        if(nodeListREF_ANNOTATION.getLength()>0 && nodeListALIGNABLE_ANNOTATION.getLength() == 0){
            for (int temp=0; temp<nodeListREF_ANNOTATION.getLength(); temp ++){
                Node tempNode = nodeListREF_ANNOTATION.item(temp);
                Element tempElement = (Element) tempNode;
                String NORMALIZE_ANNOTATION_ID = Normalizer.normalize(tempElement.getAttribute("ANNOTATION_ID").toLowerCase(), Normalizer.Form.NFD);
                String ANNOTATION_ID = NORMALIZE_ANNOTATION_ID.replaceAll("[^\\p{ASCII}]", "");

                String NORMALIZE_ANNOTATION_REF = Normalizer.normalize(tempElement.getAttribute("ANNOTATION_ID").toLowerCase(), Normalizer.Form.NFD);
                String ANNOTATION_REF = NORMALIZE_ANNOTATION_REF.replaceAll("[^\\p{ASCII}]", "");

                if(ANNOTATION_ID.equals(ANNOTATION_TIER_REF) || ANNOTATION_REF.equals(ANNOTATION_TIER_REF)){
                    NodeList childrenNodeList = tempNode.getChildNodes();

                    for (int aux_temp=0; aux_temp<childrenNodeList.getLength(); aux_temp ++) {
                        Node auxTempNode = childrenNodeList.item(aux_temp);
                        if(auxTempNode.getNodeName().equals("ANNOTATION_VALUE")){
                            auxTempNode.setTextContent(annotation);
                        }
                    }
                }
            }
        }

        if(nodeListREF_ANNOTATION.getLength() == 0 && nodeListALIGNABLE_ANNOTATION.getLength() > 0) {
            for (int temp=0; temp<nodeListALIGNABLE_ANNOTATION.getLength(); temp ++){
                Node tempNode = nodeListALIGNABLE_ANNOTATION.item(temp);
                Element tempElement = (Element) tempNode;
                String NORMALIZE_ANNOTATION_ID = Normalizer.normalize(tempElement.getAttribute("ANNOTATION_ID").toLowerCase(), Normalizer.Form.NFD);
                String ANNOTATION_ID = NORMALIZE_ANNOTATION_ID.replaceAll("[^\\p{ASCII}]", "");

                String NORMALIZE_ANNOTATION_REF = Normalizer.normalize(tempElement.getAttribute("ANNOTATION_ID").toLowerCase(), Normalizer.Form.NFD);
                String ANNOTATION_REF = NORMALIZE_ANNOTATION_REF.replaceAll("[^\\p{ASCII}]", "");

                if(ANNOTATION_ID.equals(ANNOTATION_TIER_REF) || ANNOTATION_REF.equals(ANNOTATION_TIER_REF)){
                    NodeList childrenNodeList = tempNode.getChildNodes();

                    for (int aux_temp=0; aux_temp<childrenNodeList.getLength(); aux_temp ++) {
                        Node auxTempNode = childrenNodeList.item(aux_temp);
                        if(auxTempNode.getNodeName().equals("ANNOTATION_VALUE")){
                            auxTempNode.setTextContent(annotation);
                        }
                    }
                }
            }
        }

    }
}
