package mx.geoint.Apis.Lucene;

import mx.geoint.Controllers.Logger.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@CrossOrigin(origins = {"http://infokaab.com/", "http://infokaab.com.mx/", "http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009", "http://10.2.102.182"})
@RestController
@RequestMapping(path = "api/lucene")

public class LuceneController {
    private final LuceneService luceneService;

    private Logger logger;

    public LuceneController(LuceneService luceneService){
        this.luceneService = luceneService;
        this.logger = new Logger();
    }

    @RequestMapping(path="/index", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> indexLucene(@RequestBody Map<String, String> body) throws IOException {
        String projectID = body.get("projectID");

        try{
            luceneService.indexLucene(projectID);
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SQLException", e);
        } catch (ParserConfigurationException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ParserConfigurationException", e);
        } catch (SAXException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SAXException", e);
        }
    }
}
