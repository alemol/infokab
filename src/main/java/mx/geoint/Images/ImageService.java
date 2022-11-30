package mx.geoint.Images;

import com.fasterxml.jackson.databind.ObjectMapper;
import mx.geoint.Glosa.Dictionary.DictionaryPaginate;
import mx.geoint.Model.*;
import mx.geoint.ParseXML.ParseXML;
import mx.geoint.ParseXML.Tier;
import mx.geoint.Response.ReportsResponse;
import mx.geoint.database.DBDictionary;
import mx.geoint.database.DBReports;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@Service
public class ImageService {


    public ImageService() {
    }


}
