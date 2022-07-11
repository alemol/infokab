package mx.geoint.Searcher;

import mx.geoint.Response.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SearcherService {

    @Autowired
    Searcher searcher;

    public SearchResponse findDocuments(String searchValue) {
        return searcher.find(searchValue);
    }
}
