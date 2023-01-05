package mx.geoint.Apis.Glosa;

import com.fasterxml.jackson.databind.ObjectMapper;
import mx.geoint.Controllers.WriteXML.WriteXML;
import mx.geoint.Model.Annotation.AnnotationsRequest;
import mx.geoint.Model.Annotation.AnnotationRequest;
import mx.geoint.Model.Annotation.AnnotationRegister;
import mx.geoint.Model.General.GeneralPaginateResponse;
import mx.geoint.Model.Glosado.*;
import mx.geoint.Controllers.ParseXML.ParseXML;
import mx.geoint.Model.ParseXML.Tier;
import mx.geoint.Model.Report.ReportRequest;
import mx.geoint.Model.Report.ReportsResponse;
import mx.geoint.Database.DBAnnotations;
import mx.geoint.Database.DBDictionary;
import mx.geoint.Database.DBProjects;
import mx.geoint.Database.DBReports;
import mx.geoint.pathSystem;
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
public class GlosaService {
    public static DBDictionary dbDictionary;
    public static DBProjects dbProjects;
    public static DBAnnotations dbAnnotations;

    public GlosaService() {
        this.dbDictionary = new DBDictionary();
        this.dbProjects = new DBProjects();
        this.dbAnnotations = new DBAnnotations();
    }

    /**
     * Servicio para analizar una anotacion por medio de querys
     * @param annotation anotación a analizar
     * @return ArrayList<Glosa> una lista de modelo glosa
     * @throws SQLException
     */
    public static ArrayList<GlosaResponse> textProcess(String annotation) throws SQLException {
        //String[] textList = annotation.trim().toLowerCase().replaceAll("[^a-z üáéíóúñ\'-]", "").replaceAll("\\s{2,}", " ").split(" ");
        String[] textList = annotation.trim().replaceAll("\\s{2,}", " ").split(" ");
        ArrayList<GlosaResponse> result_list = new ArrayList<>();
        ArrayList<GlosaStep> glosaSteps = new ArrayList<>();
        int i = 0;

        for (String text : textList) {
            i += 1;
            String text_process = text.toLowerCase().replaceAll("[^a-z üáéíóúñ\'-]", "");
            if(!text_process.isEmpty()){
                ArrayList<String> result = dbDictionary.textProcess(text_process);
                GlosaStep glosaStep = new GlosaStep(i, text, text_process, result);
                glosaSteps.add(glosaStep);
            }
        }
        result_list.add(new GlosaResponse(1, annotation, glosaSteps));
        return result_list;
    }

    /**
     * Servicio para analizar una lista de oraciones o anotaciones
     * @param glosaRequest una lista de annotaciones
     * @return ArrayList<Glosa> lista del modelo glosa
     * @throws SQLException
     */
    public static ArrayList<GlosaResponse> ArrayProcess(GlosaRequest glosaRequest) throws SQLException {
        ArrayList<GlosaResponse> result_list = new ArrayList<>();
        int i = 0;
        for (String annotation : glosaRequest.getAnnotations()) {
            i += 1;
            String[] textList = annotation.trim().replaceAll("\\s{2,}", " ").split(" ");
            ArrayList<GlosaStep> glosaSteps = new ArrayList<>();
            int j = 0;

            for (String text : textList) {
                j += 1;
                String text_process = text.toLowerCase().replaceAll("[^a-z üáéíóúñ\'-]", "");
                if(!text_process.isEmpty()) {
                    ArrayList<String> result = dbDictionary.textProcess(text_process);
                    GlosaStep glosaStep = new GlosaStep(j, text, text_process, result);
                    glosaSteps.add(glosaStep);
                }
            }

            result_list.add(new GlosaResponse(i, annotation, glosaSteps));
        }
        return result_list;
    }

    public Boolean saveAnnotation(AnnotationsRequest annotationsRequest) throws SQLException, ParserConfigurationException, IOException, TransformerException, SAXException {
        boolean isNew = annotationsRequest.getNew();
        if(isNew == true){
            int projectId = annotationsRequest.getProjectID();
            int glossingAnnotationInEaf = dbProjects.getGlossingAnnotationInEaf(projectId);
            return  dbProjects.setGlossingAnnotationToEaf(projectId, (glossingAnnotationInEaf+1), annotationsRequest);
        }else{
            setGlossingAnnotationToEaf(annotationsRequest);
            return  true;
        }

    }

    public void setGlossingAnnotationToEaf(AnnotationsRequest annotationsRequest) throws ParserConfigurationException, IOException, TransformerException, SAXException {
        String projectName = annotationsRequest.getFilePath();
        String annotationId = annotationsRequest.getAnnotationID();
        String annotationREF = "";
        if(annotationsRequest.getAnnotationREF().isEmpty()){
            annotationREF = annotationsRequest.getAnnotationID();
        }else{
            annotationREF = annotationsRequest.getAnnotationREF();
        }
        ArrayList<GlosaStep> steps = annotationsRequest.getSteps();

        WriteXML writeXML = new WriteXML(projectName);
        writeXML.writeElement(annotationREF, annotationId, steps);
    }


    public Boolean savedAnnotationV2(AnnotationsRequest annotationsRequest) throws SQLException, ParserConfigurationException, IOException, TransformerException, SAXException {
        boolean isNew = annotationsRequest.getNew();

        if(isNew == true){
            return dbAnnotations.newRegister(annotationsRequest);
        }else{
            return dbAnnotations.updateRegister(annotationsRequest);
        }
    }

    public Boolean savedAnnotationV3(AnnotationsRequest annotationsRequest) throws SQLException, ParserConfigurationException, IOException, TransformerException, SAXException {
        boolean isNew = annotationsRequest.getNew();

        if(isNew == true){
            return dbAnnotations.newRegisterV3(annotationsRequest);
        }else{
            return dbAnnotations.updateRegisterV3(annotationsRequest);
        }
    }

    public ArrayList<AnnotationRegister> savedAnnotationList(int project_id) throws SQLException {
        return dbAnnotations.getAnnotationList(project_id);
    }

    public AnnotationRegister getAnnotationRecord(int project_id, String annotation_ref) throws SQLException {
        return dbAnnotations.getAnnotationRecord(project_id, annotation_ref);
    }

    public Boolean saveAnnotationsToEaf(int projectID, String filePath) throws SQLException, ParserConfigurationException, IOException, TransformerException, SAXException {
        ArrayList<AnnotationRegister> annotationList = dbAnnotations.getAnnotationList(projectID);
        WriteXML parseXML = new WriteXML(filePath);
        parseXML.writeAnnotationRegisters(annotationList);
        return true;
    }
}
