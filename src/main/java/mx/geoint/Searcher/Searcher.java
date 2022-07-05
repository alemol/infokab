package mx.geoint.Searcher;

import mx.geoint.Document.LuceneResult;
import mx.geoint.Lucene.Lucene;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Searcher {

    private final Lucene lucene;

    public Searcher() {
        this.lucene = new Lucene();
    }

    // TODO: 31/05/2022
    //  Definir el método para buscar resultados en el índice de lucene
    //  Crear la clase para el objeto que se devuelve
    /**
     * Regresa la lista de resultados donde se encuentra el texto a buscar.
     * @param   text    el texto que se quiere buscar
     * @return          las incidencias con el resultado de la búsqueda
     **/
    public ArrayList<LuceneResult> find(String text) {
        try{
            ArrayList<LuceneResult> docs = lucene.searchIndex(text);
            return docs;
        } catch (IOException ex){
            return null;
        } catch (ParseException ex){
            return null;
        }
    }
}
