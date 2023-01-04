package mx.geoint.Apis.Glosa;

import com.fasterxml.jackson.databind.ObjectMapper;
import mx.geoint.Model.Dictionary.DictionaryPaginate;
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
    public static DBReports dbReports;
    public static DBProjects dbProjects;
    public static DBAnnotations dbAnnotations;

    public GlosaService() {
        this.dbDictionary = new DBDictionary();
        this.dbReports = new DBReports();
        this.dbProjects = new DBProjects();
        this.dbAnnotations = new DBAnnotations();
    }

    /**
     * Servicio que ejecuta el script de python externo a java
     * @return ArrayList<Glosa> un arreglo del modelo de glosa
     * @throws IOException
     */
    public static ArrayList<Glosa> process() throws IOException {
        String s;
        ArrayList<Glosa> result_list = new ArrayList<>();

        File f = new File("src/main/resources/");
        String absolute = f.getAbsolutePath();

        Process p = Runtime.getRuntime().exec("python3 src/main/resources/yucatec_parser.py " + absolute);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        while ((s = stdInput.readLine()) != null) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(s, Map.class);
            Glosa glosa = new Glosa((int) map.get("id"), map.get("word").toString(), (ArrayList<GlosaStep>) map.get("steps"));

            result_list.add(glosa);
        }
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }

        return result_list;
    }

    /**
     * Servicio para analizar una anotacion por medio de querys
     * @param annotation anotación a analizar
     * @return ArrayList<Glosa> una lista de modelo glosa
     * @throws SQLException
     */
    public static ArrayList<Glosa> textProcess(String annotation) throws SQLException {
        //String[] textList = annotation.trim().toLowerCase().replaceAll("[^a-z üáéíóúñ\'-]", "").replaceAll("\\s{2,}", " ").split(" ");
        String[] textList = annotation.trim().replaceAll("\\s{2,}", " ").split(" ");
        ArrayList<Glosa> result_list = new ArrayList<>();
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
        result_list.add(new Glosa(1, annotation, glosaSteps));
        return result_list;
    }

    /**
     * Servicio para analizar una lista de oraciones o anotaciones
     * @param glosaRequest una lista de annotaciones
     * @return ArrayList<Glosa> lista del modelo glosa
     * @throws SQLException
     */
    public static ArrayList<Glosa> ArrayProcess(GlosaRequest glosaRequest) throws SQLException {
        ArrayList<Glosa> result_list = new ArrayList<>();
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

            result_list.add(new Glosa(i, annotation, glosaSteps));
        }
        return result_list;
    }

    /**
     * Servicio para obtener las oraciones o anotaciones de un proyecto
     * @param filePath
     * @return ArraList<Tier> una lista del modelo Tier
     */
    public static ArrayList<Tier> getAnnotations(String filePath, String id) throws ParserConfigurationException, IOException, SAXException {
        System.out.println("data " + filePath +" id "+ id);
        String tier_id_transcripcion = pathSystem.TIER_MAIN;
        ParseXML parseXML = new ParseXML(filePath, tier_id_transcripcion);
        parseXML.read();
        return new ArrayList<>(parseXML.getTier());
    }

    public Boolean saveAnnotation(GlosaAnnotationsRequest glosaAnnotationsRequest) throws SQLException, ParserConfigurationException, IOException, TransformerException, SAXException {
        boolean isNew = glosaAnnotationsRequest.getNew();
        if(isNew == true){
            int projectId = glosaAnnotationsRequest.getProjectID();
            int glossingAnnotationInEaf = dbProjects.getGlossingAnnotationInEaf(projectId);
            return  dbProjects.setGlossingAnnotationToEaf(projectId, (glossingAnnotationInEaf+1), glosaAnnotationsRequest);
        }else{
            setGlossingAnnotationToEaf(glosaAnnotationsRequest);
            return  true;
        }

    }

    public ReportsResponse getRegisters(DictionaryPaginate dictionaryPaginate) throws SQLException {
        int page = dictionaryPaginate.getPage();
        Integer id_project = dictionaryPaginate.getId();
        int recordsPerPage = dictionaryPaginate.getRecord();
        String search = dictionaryPaginate.getSearch();

        int currentPage = (page - 1) * recordsPerPage;
        return dbReports.ListRegisters(currentPage, recordsPerPage, id_project, search);
    }

    public boolean insertRegister(ReportRequest reportRequest) throws SQLException {
        int id_project = reportRequest.getId_proyecto();
        String title = reportRequest.getTitulo();
        String report = reportRequest.getReporte();
        String tipo = reportRequest.getTipo();
        String comentario = reportRequest.getComentario();

        return dbReports.newRegister(id_project, title, report, tipo, comentario, "");
    }

    public Boolean editAnnotation(GlosaUpdateAnnotationRequest glosaUpdateAnnotationRequest) throws ParserConfigurationException, IOException, TransformerException, SAXException, SQLException {
        return dbReports.newAnnotationReport(glosaUpdateAnnotationRequest);
    }

    public void setGlossingAnnotationToEaf(GlosaAnnotationsRequest glosaAnnotationsRequest) throws ParserConfigurationException, IOException, TransformerException, SAXException {
        String projectName = glosaAnnotationsRequest.getFilePath();
        String annotationId = glosaAnnotationsRequest.getAnnotationID();
        String annotationREF = "";
        if(glosaAnnotationsRequest.getAnnotationREF().isEmpty()){
            annotationREF = glosaAnnotationsRequest.getAnnotationID();
        }else{
            annotationREF = glosaAnnotationsRequest.getAnnotationREF();
        }
        ArrayList<GlosaStep> steps = glosaAnnotationsRequest.getSteps();

        ParseXML parseXML = new ParseXML(projectName, "Glosado");
        parseXML.writeElement(annotationREF, annotationId, steps);
    }


    public Boolean savedAnnotationV2(GlosaAnnotationsRequest glosaAnnotationsRequest) throws SQLException, ParserConfigurationException, IOException, TransformerException, SAXException {
        boolean isNew = glosaAnnotationsRequest.getNew();

        if(isNew == true){
            return dbAnnotations.newRegister(glosaAnnotationsRequest);
        }else{
            return dbAnnotations.updateRegister(glosaAnnotationsRequest);
        }
    }

    public ArrayList<GlosadoAnnotationRegister> savedAnnotationList(int project_id) throws SQLException {
        return dbAnnotations.getAnnotationList(project_id);
    }

    public GlosadoAnnotationRegister getAnnotationRecord(int project_id, String annotation_ref) throws SQLException {
        return dbAnnotations.getAnnotationRecord(project_id, annotation_ref);
    }
}