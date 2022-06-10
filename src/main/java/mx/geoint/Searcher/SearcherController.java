package mx.geoint.Searcher;

import com.google.gson.Gson;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<Document>> search(@RequestBody String payload){
        final Gson gson = new Gson();
        final Search search = gson.fromJson(payload, Search.class);

        String text = search.getText();
        List<Document> documents = searcherService.findDocuments(text);

        if(documents != null){
            return ResponseEntity.status(HttpStatus.OK).body(documents);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}