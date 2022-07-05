package mx.geoint.Searcher;

import mx.geoint.Document.LuceneResult;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearcherService {

    @Autowired
    Searcher searcher;

    public ArrayList<LuceneResult> findDocuments(String searchValue) {
        return searcher.find(searchValue);
    }
}
