package mx.geoint.Apis.Searcher;

import mx.geoint.Model.SearchDoc;
import mx.geoint.Response.SearchResponse;
import mx.geoint.Lucene.Lucene;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
    public ArrayList<SearchDoc> findPage(String text, int page) throws IOException, ParseException {
        ArrayList<SearchDoc> response = lucene.searchPaginate(text, page);
        return response;
    }

    public SearchResponse findMultiple(String text) throws IOException, ParseException{
        SearchResponse response = lucene.searchMultipleIndex(text);
        return response;
    }

    public ArrayList<SearchDoc> findPageMultiple(String text, int page) throws IOException, ParseException {
        ArrayList<SearchDoc> response = lucene.searchPaginateMultiple(text, page);
        return response;
    }
}
