package mx.geoint.Apis.Searcher;

import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Model.SearchDoc;
import mx.geoint.Response.SearchResponse;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;

@CrossOrigin(origins = {"http://infokaab.com/","http://infokaab.com.mx/","http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009","http://10.2.102.182"})
@RestController
@RequestMapping(path = "api/search")
public class SearcherController {

    private final SearcherService searcherService;
    private Logger logger;
    @Autowired
    public SearcherController(SearcherService searcherService){
        this.searcherService = searcherService;
        this.logger = new Logger();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SearchResponse> search(@RequestBody Search search){
        try{
            String text = search.getText();
            SearchResponse response = searcherService.findDocuments(text);

            if(response != null){
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (IOException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontro", e);
        } catch (ParseException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al parsear", e);
        }
    }

    @RequestMapping(path = "/page", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<SearchDoc>> searchPaginate(@RequestBody SearchPage searchPage){
        String text = searchPage.getText();
        int page = searchPage.getPage();

        try{
            ArrayList<SearchDoc> response = searcherService.findDocumentsPage(text, page);

            if(response != null){
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (IOException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontro", e);
        } catch (ParseException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al parsear", e);
        }
    }

    @RequestMapping(path = "/multiple", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SearchResponse> searchMultiple(@RequestBody Search search){
        String text = search.getText();
        try{
            SearchResponse response = searcherService.findDocumentsMultiple(text);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IOException e){
            System.out.println("entre IO");
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró", e);
        } catch (ParseException e){
            System.out.println("entre Parse");
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al parsear", e);
        }
    }

    @RequestMapping(path = "/multiple/page", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<SearchDoc>> searchPaginateMultiple(@RequestBody SearchPage searchPage){
        String text = searchPage.getText();
        int page = searchPage.getPage();
        try{
            ArrayList<SearchDoc> response = searcherService.findDocumentsPageMultiple(text, page);

            if(response != null){
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (IOException e) {
            Logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró", e);
        } catch (ParseException e) {
            Logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al parsear", e);
        }
    }
}