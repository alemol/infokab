package mx.geoint.Searcher;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
    public List<Document> search(@RequestBody String text){
        try{
            List<Document> documents = searcherService.findDocuments(text);
            return documents;
        }
        catch (IOException ex){
            return null;
        }
        catch (ParseException ex){
            return null;
        }
    }
}