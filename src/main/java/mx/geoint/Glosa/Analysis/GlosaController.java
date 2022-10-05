package mx.geoint.Glosa.Analysis;

import mx.geoint.Model.Glosa;
import mx.geoint.Model.GlosaRequest;
import mx.geoint.ParseXML.Tier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@CrossOrigin(origins = {"http://infokaab.com/","http://infokaab.com.mx/","http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009","http://10.2.102.182"})
@RestController
@RequestMapping(path = "api/glosa")
public class GlosaController {
    private final GlosaService glosaService;

    public GlosaController(GlosaService glosaService) {
        this.glosaService = glosaService;
    }


    @RequestMapping(path="/script", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<Glosa>> created() throws IOException {
        Date startDate = new Date();
        ArrayList<Glosa> response = glosaService.process();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @RequestMapping(path="/analysis", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<Glosa>> analysis(@RequestBody Map<String, String> body) throws IOException, SQLException {
        //System.out.println("data:"+body.get("uuid"));
        String text = body.get("text");
        ArrayList<Glosa> response = glosaService.textProcess(text);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(path="/analysis/list", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<Glosa>> arrayAnalisys(@RequestBody GlosaRequest glosaRequest) throws IOException, SQLException {
        ArrayList<Glosa> response = glosaService.ArrayProcess(glosaRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(path="/projects", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<String>> getFiles() throws IOException {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("04_02_01062022_11_SCY_C_2_2");
        arrayList.add("04_02_01072022_11_SCY_C_2_2");
        arrayList.add("04_02_01082022_11_SCY_C_2_2");
        arrayList.add("04_02_01092022_11_SCY_C_2_2");
        return ResponseEntity.status(HttpStatus.OK).body(arrayList);
    }

    @RequestMapping(path="/annotations", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<Tier>> getAnnotations(@RequestBody Map<String, String> body) throws IOException {
        String project = body.get("project");
        ArrayList<Tier> arrayList = glosaService.getAnnotations(project);
        return ResponseEntity.status(HttpStatus.OK).body(arrayList);
    }
}
