package mx.geoint.glosa;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "api/glosa")
public class GlosaController {
    private final GlosaService glosaService;

    public GlosaController(GlosaService glosaService) {
        this.glosaService = glosaService;
    }


    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<String>> created() throws IOException {
        Date startDate = new Date();
        ArrayList<String> response = glosaService.process();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
