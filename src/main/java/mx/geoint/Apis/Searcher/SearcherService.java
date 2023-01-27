package mx.geoint.Apis.Searcher;

import mx.geoint.Model.Search.SearchLuceneDoc;
import mx.geoint.Model.Search.SearchResponse;
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
        return searcher.find(formatLucene(searchValue));
    }

    public ArrayList<SearchLuceneDoc> findDocumentsPage(String searchvalue, int page) throws IOException, ParseException {
        return searcher.findPage(formatLucene(searchvalue), page);
    }

    public SearchResponse findDocumentsMultiple(String searchValue, String index) throws IOException, ParseException {
        return  searcher.findMultiple(formatLucene(searchValue), index);
    }

    public ArrayList<SearchLuceneDoc> findDocumentsPageMultiple(String searchValue, int page, String index) throws IOException, ParseException {
        return searcher.findPageMultiple(formatLucene(searchValue), page, index);
    }

    public String formatLucene(String text){
        String new_text = text;
        new_text = new_text.replaceAll("\\+","\\\\+");
        new_text = new_text.replaceAll("\\-","\\\\-");
        new_text = new_text.replaceAll("\\&\\&","\\\\&&");
        new_text = new_text.replaceAll("\\|\\|","\\\\||");
        new_text = new_text.replaceAll("\\!","\\\\!");
        new_text = new_text.replaceAll("\\(","\\\\(");
        new_text = new_text.replaceAll("\\)","\\\\)");
        new_text = new_text.replaceAll("\\{","\\\\{");
        new_text = new_text.replaceAll("\\}","\\\\}");
        new_text = new_text.replaceAll("\\[","\\\\[");
        new_text = new_text.replaceAll("\\]","\\\\]");
        new_text = new_text.replaceAll("\\^","\\\\^");
        new_text = new_text.replaceAll("\\“","\\\\“");
        new_text = new_text.replaceAll("\\~","\\\\~");
        new_text = new_text.replaceAll("\\*","\\\\*");
        new_text = new_text.replaceAll("\\?","\\\\?");
        new_text = new_text.replaceAll("\\:","\\\\:");
        return new_text;
    }
}
