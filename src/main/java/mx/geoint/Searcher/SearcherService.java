package mx.geoint.Searcher;

import mx.geoint.Result.LuceneResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SearcherService {

    @Autowired
    Searcher searcher;

    public ArrayList<LuceneResult> findDocuments(String searchValue) {
        return searcher.find(searchValue);
    }
}
