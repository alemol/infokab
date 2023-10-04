package mx.geoint.Apis.Searcher;

import mx.geoint.Database.DBSearch;
import mx.geoint.Model.Project.ProjectPostgresGeometry;
import mx.geoint.Model.Project.ProjectPostgresLocations;
import mx.geoint.Model.Search.SearchLuceneDoc;
import mx.geoint.Model.Search.SearchPostgres;
import mx.geoint.Model.Search.SearchResponse;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class SearcherService {

    @Autowired
    Searcher searcher;

    public static DBSearch dbSearch;

    public SearcherService(){
        this.dbSearch = new DBSearch();
    }

    public SearchResponse findDocuments(String searchValue) throws IOException, ParseException, SQLException {
        return searcher.find(formatLucene(searchValue));
    }

    public ArrayList<SearchLuceneDoc> findDocumentsPage(String searchvalue, int page) throws IOException, ParseException, SQLException {
        return searcher.findPage(formatLucene(searchvalue), page);
    }

    public SearchResponse findDocumentsMultiple(String searchValue, String index, ArrayList<String> cvegeo, boolean levenshtein, String id_usuario) throws IOException, ParseException, SQLException {
        return  searcher.findMultiple(formatLucene(searchValue), index, cvegeo, levenshtein, id_usuario);
    }

    public ArrayList<SearchLuceneDoc> findDocumentsPageMultiple(String searchValue, int page, String index, ArrayList<String> cvegeo, boolean levenshtein, boolean isMap) throws IOException, ParseException, SQLException {
        return searcher.findPageMultiple(formatLucene(searchValue), page, index, cvegeo, levenshtein, isMap);
    }


    public ProjectPostgresGeometry findMultipleLocations(String searchValue, String index, boolean levenshtein) throws IOException, ParseException, SQLException {
        return  searcher.findMultipleLocations(formatLucene(searchValue), index, levenshtein);
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

    public ArrayList<SearchPostgres> getSearchs() throws SQLException {
        ArrayList<SearchPostgres> result = this.dbSearch.listSearch();
        return result;
    }
}
