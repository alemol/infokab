package mx.geoint.Searcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearcherService {

    @Autowired
    Searcher searcher;

    public String[] findDocuments(String searchValue){
        return searcher.find(searchValue);
    }
}
