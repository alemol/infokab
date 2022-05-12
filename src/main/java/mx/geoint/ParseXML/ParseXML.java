package mx.geoint.ParseXML;

import java.io.File;
import java.io.IOException;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ParseXML {
    public static void main(String [] arg) {
        try{
            String filePath = "src/main/resources/eligio_uikab_mena.eaf";
            File inputFile = new File(filePath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            ParseHandler parseHandler = new ParseHandler();
            saxParser.parse(inputFile, parseHandler);

            parseHandler.getTranscripcion();
            parseHandler.getTraduccion();
            parseHandler.getGlosado();
            parseHandler.getMorfemas();

            //Gson gson = new Gson();
            //String json = gson.toJson(parseHandler);
            //System.out.println("Transcripción: " + json);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
