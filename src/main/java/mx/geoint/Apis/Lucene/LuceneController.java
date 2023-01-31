package mx.geoint.Apis.Lucene;

import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Controllers.Lucene.ThreadLuceneIndex;
import mx.geoint.Model.Lucene.LuceneProjectRequest;
import mx.geoint.Model.Lucene.LuceneProjectsRequest;
import mx.geoint.pathSystem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@CrossOrigin(origins = {"http://infokaab.com/", "http://infokaab.com.mx/", "http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009", "http://10.2.102.182"})
@RestController
@RequestMapping(path = "api/lucene")
public class LuceneController {
    private final ThreadLuceneIndex threadLuceneIndex;
    private final LuceneService luceneService;

    private Logger logger;

    public LuceneController(LuceneService luceneService){
        this.luceneService = luceneService;
        this.logger = new Logger();
        threadLuceneIndex = new ThreadLuceneIndex();
        threadLuceneIndex.start();
    }

    @RequestMapping(path="/index", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> indexLucene(@RequestBody Map<String, String> body) throws IOException {
        String projectID = body.get("projectID");
        String indexName = "ALL";

        try{
            luceneService.indexLucene(projectID, indexName);
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

    @RequestMapping(path="/projects/index/v1", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> indexArrayLucene(@RequestBody LuceneProjectsRequest luceneProjectsRequest) throws IOException {
        try{
            ArrayList<LuceneProjectRequest> luceneProjectRequests = luceneProjectsRequest.getProjectIndex();
            for(LuceneProjectRequest project : luceneProjectRequests){
                boolean maya = project.getMaya();
                boolean spanish = project.getSpanish();
                boolean glosa = project.getGlosa();
                if(maya){
                    luceneService.indexProjectLucene(project.getProjectID(),pathSystem.INDEX_LANGUAJE_MAYA);
                }
                if(spanish){
                    luceneService.indexProjectLucene(project.getProjectID(),pathSystem.INDEX_LANGUAJE_SPANISH);
                }
                if(glosa){
                    luceneService.indexProjectLucene(project.getProjectID(), pathSystem.INDEX_LANGUAJE_GLOSA);
                }
            }
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

    @RequestMapping(path="/projects/index/v2", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> indexArrayLuceneV2(@RequestBody LuceneProjectsRequest luceneProjectsRequest) throws IOException {
        ArrayList<LuceneProjectRequest> luceneProjectRequests = luceneProjectsRequest.getProjectIndex();
        for(LuceneProjectRequest project : luceneProjectRequests){
            threadLuceneIndex.add(project);
            threadLuceneIndex.activate();
        }
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
