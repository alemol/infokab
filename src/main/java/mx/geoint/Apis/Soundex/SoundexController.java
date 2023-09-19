package mx.geoint.Apis.Soundex;

import mx.geoint.Apis.Searcher.SearcherService;
import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Model.Search.SearchLuceneDoc;
import mx.geoint.Model.Search.SearchPage;
import mx.geoint.Model.Soundex.SoundexResponse;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@CrossOrigin(origins = {"http://taantsil.com","http://taantsil.com.mx/","http://infokaab.com/","http://infokaab.com.mx/","http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009","http://10.2.102.182"})
@RestController
@RequestMapping(path = "api/soundex")
public class SoundexController {
    private final SoundexService SoundexService;
    private Logger logger;
    @Autowired
    public SoundexController(SoundexService soundexService){
        this.SoundexService = soundexService;
        this.logger = new Logger();
    }


    @RequestMapping(path = "/word", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SoundexResponse> searchPaginate(@RequestBody Map<String, String> body) {
        String word = body.get("text");

        SoundexResponse response = SoundexService.getWord(word);

        if (response != null) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}