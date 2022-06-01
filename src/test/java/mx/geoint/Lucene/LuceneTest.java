package mx.geoint.Lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

class LuceneTest {
    private Lucene lucene;

    void initLucene(){
        lucene = new Lucene();
    }

    @Test
    void createIndex() throws IOException {
        initLucene();
        lucene.createIndex("/home/alejandro/Documentos/projects/infokab-backend/Files/file_to_index");
    }

    @Test
    void searchIndex() throws IOException, ParseException {
        initLucene();
        List<Document> docs = lucene.searchIndex("mutación de virus");
    }
}