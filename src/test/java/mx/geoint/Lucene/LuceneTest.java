package mx.geoint.Lucene;

import mx.geoint.pathSystem;
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
        lucene.initConfig(true);
        lucene.createIndex(pathSystem.DIRECTORY_FILES_JSON);
    }

    @Test
    void createIndexTwoDirectory() throws IOException {
        Lucene lucene_1 = new Lucene(pathSystem.DIRECTORY_INDEX_GENERAL+"index1/");
        Lucene lucene_2 = new Lucene(pathSystem.DIRECTORY_INDEX_GENERAL+"index2/");
        lucene_1.initConfig(true);
        lucene_2.initConfig(true);
        lucene_1.createIndex(pathSystem.DIRECTORY_FILES_JSON);
        lucene_2.createIndex(pathSystem.DIRECTORY_FILES_JSON);
    }

    @Test
    void searchIndex() throws IOException, ParseException {
        initLucene();
        List<Document> docs = lucene.searchIndex("weye\u0027 bajux");
        //List<Document> docs = lucene.searchIndex("tsíimino’ob");
    }

    @Test
    void searchMutlipleIndex() throws IOException, ParseException {
        Lucene lucene = new Lucene();
        lucene.searchMultipleIndex("weye\u0027 bajux");
    }
}