package mx.geoint.Glosa.Analysis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mx.geoint.Model.Glosa;
import mx.geoint.Model.GlosaStep;
import mx.geoint.database.DBDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GlosaService {
    public static DBDictionary dbDictionary;
    public GlosaService(){
        this.dbDictionary = new DBDictionary();
    }

    public static ArrayList<Glosa> process() throws IOException {
        String s;
        ArrayList<Glosa> result_list = new ArrayList<>();

        File f = new File("src/main/resources/");
        String absolute = f.getAbsolutePath();

        Process p = Runtime.getRuntime().exec("python3 src/main/resources/yucatec_parser.py "+absolute);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        while ((s = stdInput.readLine()) != null) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(s, Map.class);
            Glosa glosa = new Glosa((int)map.get("id"), map.get("word").toString(), (ArrayList<GlosaStep>) map.get("steps"));

            result_list.add(glosa);
        }
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }

        return result_list;
    }

    public static ArrayList<Glosa> textProcess(String annotation) throws SQLException {
        String[] textList = annotation.split(" ");
        ArrayList<Glosa> result_list = new ArrayList<>();
        ArrayList<GlosaStep> glosaSteps = new ArrayList<>();
        int i = 0;

        for (String text : textList){
            i += 1;
            ArrayList<String> result = dbDictionary.textProcess(text);
            GlosaStep glosaStep = new GlosaStep(i, text, result);
            glosaSteps.add(glosaStep);
        }
        result_list.add(new Glosa(1, annotation, glosaSteps));
        System.out.println(result_list.get(0).getSteps().toString());
        return result_list;
    }
}
