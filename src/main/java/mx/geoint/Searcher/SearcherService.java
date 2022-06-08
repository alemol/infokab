package mx.geoint.Searcher;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SearcherService {

    @Autowired
    Searcher searcher;

    public List<Document> findDocuments(String searchValue) throws IOException, ParseException {
        return searcher.find(searchValue);
    }
}
