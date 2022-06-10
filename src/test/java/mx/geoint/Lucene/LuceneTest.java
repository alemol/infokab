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
        lucene.createIndex("/home/jose/Documentos/CentroGeo/Infokab/Files/file_to_index_json", true);
    }

    @Test
    void searchIndex() throws IOException, ParseException {
        initLucene();
        List<Document> docs = lucene.searchIndex("weye\u0027 bajux");
        //List<Document> docs = lucene.searchIndex("tsíimino’ob");
    }
}