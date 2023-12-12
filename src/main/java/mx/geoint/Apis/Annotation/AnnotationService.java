package mx.geoint.Apis.Annotation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import mx.geoint.Controllers.ParseXML.ParseXML;
import mx.geoint.Database.DBAnnotations;
import mx.geoint.Database.DBDictionary;
import mx.geoint.Database.DBProjects;
import mx.geoint.Database.DBReports;
import mx.geoint.Model.Annotation.AnnotationRequest;
import mx.geoint.Model.ParseXML.Tier;
import mx.geoint.Model.ParseXML.TierMultiple;
import mx.geoint.pathSystem;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

@Service
public class AnnotationService {
    public static DBReports dbReports;

    public AnnotationService(){
        this.dbReports = new DBReports();
    }

    public Boolean editAnnotation(AnnotationRequest annotationRequest) throws ParserConfigurationException, IOException, TransformerException, SAXException, SQLException {
        return dbReports.newAnnotationReport(annotationRequest);
    }

    /**
     * Servicio para obtener las oraciones o anotaciones de un proyecto
     * @param filePath
     * @return ArraList<Tier> una lista del modelo Tier
     */
    public static ArrayList<Tier> getAnnotations(String filePath, String id) throws ParserConfigurationException, IOException, SAXException {
        System.out.println("data " + filePath + " id " + id);
        String tier_id_transcripcion = pathSystem.TIER_MAIN;
        ParseXML parseXML = new ParseXML(filePath, tier_id_transcripcion);
        parseXML.read();
        return new ArrayList<>(parseXML.getTier());
    }

    public static ArrayList<TierMultiple> getMultipleAnnotations(String filePath, String id) throws ParserConfigurationException, IOException, SAXException {

        ArrayList<TierMultiple> list_tier_multiple = new ArrayList<>();

        System.out.println("data " + filePath + " id " + id);
        String tier_id_transcripcion = pathSystem.TIER_MAIN;
        ParseXML parseXML = new ParseXML(filePath, tier_id_transcripcion);
        parseXML.readAnnotations();

        JsonObject getMultipleTier = parseXML.getTierMultipleAnnotations();
        //String baseNameEaf = FilenameUtils.getBaseName(filePath);
        Iterator<String> iterator = getMultipleTier.keySet().iterator();


        while (iterator.hasNext()) {
            String key = iterator.next();
            Gson gson = new Gson();
            TierMultiple tierMultiple = gson.fromJson(getMultipleTier.getAsJsonObject(key).toString(), TierMultiple.class);
            list_tier_multiple.add(tierMultiple);
        }
        return list_tier_multiple;
    }

}
