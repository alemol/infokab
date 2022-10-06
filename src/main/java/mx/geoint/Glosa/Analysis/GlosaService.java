package mx.geoint.Glosa.Analysis;

import com.fasterxml.jackson.databind.ObjectMapper;
import mx.geoint.Model.Glosa;
import mx.geoint.Model.GlosaAnnotationsRequest;
import mx.geoint.Model.GlosaRequest;
import mx.geoint.Model.GlosaStep;
import mx.geoint.ParseXML.ParseXML;
import mx.geoint.ParseXML.Tier;
import mx.geoint.database.DBDictionary;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class GlosaService {
    public static DBDictionary dbDictionary;

    public GlosaService() {
        this.dbDictionary = new DBDictionary();
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
        String[] textList = annotation.trim().replaceAll("\\s{2,}", " ").split(" ");
        ArrayList<Glosa> result_list = new ArrayList<>();
        ArrayList<GlosaStep> glosaSteps = new ArrayList<>();
        int i = 0;

        for (String text : textList) {
            i += 1;
            ArrayList<String> result = dbDictionary.textProcess(text);
            GlosaStep glosaStep = new GlosaStep(i, text, result);
            glosaSteps.add(glosaStep);
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
                ArrayList<String> result = dbDictionary.textProcess(text);
                GlosaStep glosaStep = new GlosaStep(j, text, result);
                glosaSteps.add(glosaStep);
            }

            result_list.add(new Glosa(i, annotation, glosaSteps));
        }
        return result_list;
    }

    /**
     * Servicio para obtener las oraciones o anotaciones de un proyecto
     * @param name
     * @return ArraList<Tier> una lista del modelo Tier
     */
    public static ArrayList<Tier> getAnnotations(String name) {
        String path = "";
        String tier_id_transcripcion = "";

        switch (name) {
            case "04_02_01062022_11_SCY_C_2_2":
                //path = "Pablo Balam 1_RIKT.eaf";
                path = "04_02_01062022_11_SCY_C_2_2.eaf";
                tier_id_transcripcion = "Transcripcion ";
                break;
            case "04_02_01072022_11_SCY_C_2_2":
                //path = "Juan Tuyub_FCA.eaf";
                path = "04_02_01072022_11_SCY_C_2_2.eaf";
                tier_id_transcripcion = "Transcripción Ortográfico";
                break;
            case "04_02_01082022_11_SCY_C_2_2":
                //path = "22-08-2022 ALONDRA-XOHUAYAN_IPC respaldo.eaf";
                path = "04_02_01082022_11_SCY_C_2_2.eaf";
                tier_id_transcripcion = "transpcion ortografica";
                break;
            case "04_02_01092022_11_SCY_C_2_2":
                //path = "2015-01-09_1650_Entrevista_datos_espontáneos_Clementina.eaf";
                path = "04_02_01092022_11_SCY_C_2_2.eaf";
                tier_id_transcripcion = "oracion";
                break;
        }

        return new ArrayList<>(getTierDinamic("./eafs/" + path, tier_id_transcripcion));
    }

    /**
     * Servicio para iniciar y ejecutar la lectura de las anotaciones de un archivo eaf
     * @param path_eaf ruta del archivo eaf
     * @param tier_id id de la capa tier a obtener
     * @return List<Tier> una lista de las anotaciones del archivo eaf
     */
    static List<Tier> getTierDinamic(String path_eaf, String tier_id) {
        ParseXML parseXML = new ParseXML(path_eaf, tier_id);
        parseXML.read();
        return parseXML.getTier();
    }


    public Boolean saveAnnotation(GlosaAnnotationsRequest glosaAnnotationsRequest) throws ParserConfigurationException, IOException, TransformerException, SAXException {
        String projectName = glosaAnnotationsRequest.getProject();
        String annotationId = glosaAnnotationsRequest.getAnnotationID();
        String annotationREF = glosaAnnotationsRequest.getAnnotationREF();
        ArrayList<GlosaStep> steps = glosaAnnotationsRequest.getSteps();
        ParseXML parseXML = new ParseXML("./eafs/"+projectName+".eaf", "Glosado");
        parseXML.writeElement(annotationREF, annotationId, steps);
        return true;
    }
}
