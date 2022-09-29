package mx.geoint.Glosa.Analysis;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mx.geoint.Model.Glosa;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:3009, http://localhost:3009","http://localhost:3000", "http://10.2.102.182:3009","http://10.2.102.182"})
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
    public ResponseEntity<ArrayList<Glosa>> analysis(@RequestBody Map<String, String> body) throws IOException {
        System.out.println("data:"+body.get("text"));
        System.out.println("data:"+body.get("uuid"));
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<Glosa>());
    }
}
