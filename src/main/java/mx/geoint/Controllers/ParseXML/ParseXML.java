package mx.geoint.Controllers.ParseXML;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mx.geoint.Model.Glosado.GlosaStep;
import mx.geoint.Model.ParseXML.Tier;
import mx.geoint.pathSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class ParseXML {
    String filePath = "";
    String name = "";
    ParseHandler parseHandler;

    public ParseXML(String path) {
        filePath = path;
        parseHandler = new ParseHandler();
    }

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
    public void read() throws ParserConfigurationException, SAXException, IOException{
        File inputFile = new File(filePath);
        name = inputFile.getName();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(inputFile, parseHandler);
    }


    /**
     * Regresar una lista de la clase tier que se obtuvo de parse del archivo .eaf
     * @return List<Tier> Lista de instancias de la clase Tier
     */
    public List<Tier> getTier(){
        return parseHandler.getTier();
    }

    public Map<String, List<Tier>> getTiers(){
        return parseHandler.getTiers();
    }

    /**
     * Regresa el mimeType del archivo multimedia obtenido del archivo eaf
     * @return String, mimeType del archivo multimedia en el archivo eaf
     */
    public String getMimeType(){
        return parseHandler.getMimeType();
    }
}
