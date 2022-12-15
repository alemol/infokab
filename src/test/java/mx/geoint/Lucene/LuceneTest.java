package mx.geoint.Lucene;

import mx.geoint.Controllers.Lucene.Lucene;
import mx.geoint.Controllers.ElanXmlDigester.ElanXmlDigester;
import mx.geoint.Model.SearchDoc;
import mx.geoint.Response.SearchResponse;
import mx.geoint.pathSystem;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

class LuceneTest {
    private Lucene lucene;

    void initLucene(){
        lucene = new Lucene();
    }

    public ElanXmlDigester initElanXmlDigesterAudio(String uuid){
        return new ElanXmlDigester("src/main/resources/eligio_uikab_mena.eaf", uuid);
    }

    @Test
    void createIndex() throws ParserConfigurationException, SAXException, IOException{
        ElanXmlDigester user = initElanXmlDigesterAudio("47eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
        user.parse_tier("oracion", true, false);

        Lucene lucene = new Lucene(pathSystem.DIRECTORY_INDEX_GENERAL+user.getUUID()+"/");
        lucene.initConfig(true);
        lucene.createIndex(user.basePathJsonFiles());
    }

    @Test
    void createIndexTwoDirectory() throws ParserConfigurationException, SAXException, IOException {
        ElanXmlDigester user1 = initElanXmlDigesterAudio("47eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
        ElanXmlDigester user2 = initElanXmlDigesterAudio("47eebc99-9c0b-4ef8-bb6d-6bb9bd380a00");

        user1.parse_tier("oracion", true, false);
        user2.parse_tier("oracion", true, false);

        Lucene lucene_1 = new Lucene(pathSystem.DIRECTORY_INDEX_GENERAL+user1.getUUID()+"/");
        Lucene lucene_2 = new Lucene(pathSystem.DIRECTORY_INDEX_GENERAL+user2.getUUID()+"/");
        lucene_1.initConfig(true);
        lucene_2.initConfig(true);
        lucene_1.createIndex(user1.basePathJsonFiles());
        lucene_2.createIndex(user2.basePathJsonFiles());
    }

    @Test
    void searchIndex() throws IOException, ParseException {
        initLucene();
        SearchResponse docs = lucene.searchIndex("weye\u0027 bajux");
        //List<Document> docs = lucene.searchIndex("tsíimino’ob");
    }

    @Test
    void searchPaginate() throws IOException, ParseException {
        initLucene();
        ArrayList<SearchDoc> docs = lucene.searchPaginate("ma'", 1);
    }

    @Test
    void searchMutlipleIndex() throws IOException, ParseException {
        //createIndex();
        Lucene lucene = new Lucene();
        lucene.searchMultipleIndex("ch´aalun");
    }

    @Test
    void createIndexLuceneText() throws IOException {
        Lucene lucene_1 = new Lucene(pathSystem.DIRECTORY_INDEX_GENERAL+"47eebc99-9c0b-4ef8-bb6d-6bb9bd380a11/");
        lucene_1.initConfig(true);
        IndexWriter indexWriter = lucene_1.statusIndexWrite();

        Document document1 = new Document();
        document1.add(new TextField("contents", "bueeno tuun ch´aalun", Field.Store.YES));
        indexWriter.addDocument(document1);

        Document document2 = new Document();
        document2.add(new TextField("contents", "bueeno tuun ch'aalun", Field.Store.YES));
        indexWriter.addDocument(document2);
        indexWriter.close();
    }
}