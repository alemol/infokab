package mx.geoint.glosa;

import com.fasterxml.jackson.databind.ObjectMapper;
import mx.geoint.Model.Glosa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GlosaService {
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
            Glosa glosa = new Glosa((int)map.get("id"), map.get("word").toString(), map.get("steps"));

            result_list.add(glosa);
        }
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }

        return result_list;
    }
}
