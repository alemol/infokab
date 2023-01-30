package mx.geoint.Apis.Searcher;

import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Model.Search.SearchRequest;
import mx.geoint.Model.Search.SearchPage;
import mx.geoint.Model.Search.SearchLuceneDoc;
import mx.geoint.Model.Search.SearchResponse;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.SQLException;
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
    public ResponseEntity<SearchResponse> search(@RequestBody SearchRequest searchRequest){
        try{
            String text = searchRequest.getText();
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
    public ResponseEntity<ArrayList<SearchLuceneDoc>> searchPaginate(@RequestBody SearchPage searchPage){
        String text = searchPage.getText();
        int page = searchPage.getPage();

        try{
            ArrayList<SearchLuceneDoc> response = searcherService.findDocumentsPage(text, page);

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
    public ResponseEntity<SearchResponse> searchMultiple(@RequestBody SearchRequest searchRequest){
        try{
            String text = searchRequest.getText();
            String index = searchRequest.getIndex();

            SearchResponse response = searcherService.findDocumentsMultiple(text, index);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IOException e){
            System.out.println("entre IO");
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró", e);
        } catch (ParseException e){
            System.out.println("entre Parse");
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al parsear", e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(path = "/multiple/page", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<SearchLuceneDoc>> searchPaginateMultiple(@RequestBody SearchPage searchPage){
        String text = searchPage.getText();
        String index = searchPage.getIndex();
        int page = searchPage.getPage();
        try{
            ArrayList<SearchLuceneDoc> response = searcherService.findDocumentsPageMultiple(text, page, index);

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