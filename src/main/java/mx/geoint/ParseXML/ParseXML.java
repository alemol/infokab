package mx.geoint.ParseXML;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

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
