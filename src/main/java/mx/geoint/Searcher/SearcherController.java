package mx.geoint.Searcher;

import com.google.gson.Gson;
import mx.geoint.Model.SearchDoc;
import mx.geoint.Response.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@CrossOrigin(origins = {"http://localhost:3000", "http://10.2.102.202:3000/"})
@RestController
@RequestMapping(path = "api/search")
public class SearcherController {

    private final SearcherService searcherService;

    @Autowired
    public SearcherController(SearcherService searcherService){
        this.searcherService = searcherService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SearchResponse> search(@RequestBody Search search){
        String text = search.getText();
        SearchResponse response = searcherService.findDocuments(text);

        if(response != null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @RequestMapping(path = "/page", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<SearchDoc>> searchPaginate(@RequestBody SearchPage searchPage){
        String text = searchPage.getText();
        int page = searchPage.getPage();

        ArrayList<SearchDoc> response = searcherService.findDocumentsPage(text, page);

        if(response != null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}