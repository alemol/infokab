package mx.geoint.Apis.Searcher;

import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Model.Project.ProjectPostgresGeometry;
import mx.geoint.Model.Project.ProjectPostgresLocations;
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

@CrossOrigin(origins = {"http://taantsil.com","http://taantsil.com.mx/","http://infokaab.com/","http://infokaab.com.mx/","http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009","http://10.2.102.182","http://10.2.102.189:3009"})
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
        } catch (SQLException e) {
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
        } catch (SQLException e) {
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
            ArrayList<String> cvegeo = searchRequest.getCvegeo();
            boolean levenshtein = searchRequest.isLevenshtein();
            System.out.println("CVEGEO "+  cvegeo);
            String id_usuario = searchRequest.getId_usuario();
            SearchResponse response = searcherService.findDocumentsMultiple(text, index, cvegeo, levenshtein, id_usuario);
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
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al parsear", e);
        }
    }

    @RequestMapping(path = "/multiple/page", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<SearchLuceneDoc>> searchPaginateMultiple(@RequestBody SearchPage searchPage){
        String text = searchPage.getText();
        String index = searchPage.getIndex();
        ArrayList<String> cvegeo = searchPage.getCvegeo();
        boolean isMap = searchPage.isMap();

        int page = searchPage.getPage();
        boolean levenshtein = searchPage.isLevenshtein();
        try{
            ArrayList<SearchLuceneDoc> response = searcherService.findDocumentsPageMultiple(text, page, index, cvegeo, levenshtein, isMap);

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
        } catch (SQLException e) {
            Logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al parsear", e);
        }
    }

    @RequestMapping(path = "/locations", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProjectPostgresGeometry> searchLocations(@RequestBody SearchRequest searchRequest){
        try{
            String text = searchRequest.getText();
            String index = searchRequest.getIndex();
            boolean levenshtein = searchRequest.isLevenshtein();

            ProjectPostgresGeometry response = searcherService.findMultipleLocations(text, index, levenshtein);
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
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al parsear", e);
        }
    }

}