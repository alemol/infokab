package mx.geoint.Apis.Searcher;

import mx.geoint.Model.Search.SearchResponse;
import mx.geoint.Model.Search.SearchLuceneDoc;
import mx.geoint.Controllers.Lucene.Lucene;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
public class Searcher {

    private final Lucene lucene;

    public Searcher() {
        this.lucene = new Lucene();
    }

    /**
     * Regresa la lista de resultados donde se encuentra el texto a buscar.
     * @param   text    el texto que se quiere buscar
     * @return          las incidencias con el resultado de la búsqueda
     **/
    public SearchResponse find(String text) throws IOException, ParseException {
        SearchResponse response = lucene.searchIndex(text);
        return response;
    }

    /**
     * Regresa la lista de resultados correspondiente a la página donde se encuentra el texto a buscar
     * @param   text    el texto que se quiere buscar
     * @param   page    la página de resultados que se necesita
     * @return          las incidencias con el resultado de la búsqueda
     */
    public ArrayList<SearchLuceneDoc> findPage(String text, int page) throws IOException, ParseException {
        ArrayList<SearchLuceneDoc> response = lucene.searchPaginate(text, page);
        return response;
    }

    public SearchResponse findMultiple(String text) throws IOException, ParseException, SQLException {
        SearchResponse response = lucene.searchMultipleIndex(text);
        return response;
    }

    public ArrayList<SearchLuceneDoc> findPageMultiple(String text, int page) throws IOException, ParseException {
        ArrayList<SearchLuceneDoc> response = lucene.searchPaginateMultiple(text, page);
        return response;
    }
}
