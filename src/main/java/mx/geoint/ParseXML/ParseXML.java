package mx.geoint.ParseXML;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ParseXML {
    String filePath = "";
    ParseHandler parseHandler = new ParseHandler();

    public ParseXML(String path) {
        filePath = path;
    }

    public void read() {
        try{
            File inputFile = new File(filePath);
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

    public List<Tier> getTierTranscription(){
        return parseHandler.getTranscripcion();
    }

    public List<Tier> getTierTraduccion(){
        return parseHandler.getTraduccion();
    }

    public List<Tier> getTierGlosado(){
        return parseHandler.getGlosado();
    }

    public List<Tier> getTierMorfemas(){
        return parseHandler.getMorfemas();
    }
}
