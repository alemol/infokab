package mx.geoint.Searcher;

import mx.geoint.Model.SearchDoc;
import mx.geoint.Response.SearchResponse;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class SearcherService {

    @Autowired
    Searcher searcher;

    public SearchResponse findDocuments(String searchValue) throws IOException, ParseException {
        return searcher.find(searchValue);
    }

    public ArrayList<SearchDoc> findDocumentsPage(String searchvalue, int page) throws IOException, ParseException {
        return searcher.findPage(searchvalue, page);
    }

    public SearchResponse findDocumentsMultiple(String searchValue) throws IOException, ParseException { return  searcher.findMultiple(searchValue); }

    public ArrayList<SearchDoc> findDocumentsPageMultiple(String searchValue, int page) throws IOException, ParseException {
        return searcher.findPageMultiple(searchValue, page);
    }
}
