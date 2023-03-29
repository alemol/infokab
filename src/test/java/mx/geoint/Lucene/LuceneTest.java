package mx.geoint.Lucene;

import ch.qos.logback.core.util.FileUtil;
import mx.geoint.Apis.Searcher.Searcher;
import mx.geoint.Controllers.Lucene.Lucene;
import mx.geoint.Controllers.ElanXmlDigester.ElanXmlDigester;
import mx.geoint.Model.Search.SearchLuceneDoc;
import mx.geoint.Model.Search.SearchResponse;
import mx.geoint.pathSystem;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.grouping.GroupingSearch;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

class LuceneTest {
    private Lucene lucene;

    void initLucene(){
        lucene = new Lucene();
    }

    public ElanXmlDigester initElanXmlDigesterAudio(String uuid){
        return new ElanXmlDigester("src/main/resources/eligio_uikab_mena.eaf", uuid);
    }

    @Test
    void createIndex() throws ParserConfigurationException, SAXException, IOException, SQLException {
        ElanXmlDigester user = initElanXmlDigesterAudio("47eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
        user.parse_tier("oracion", true, false);

        Lucene lucene = new Lucene(pathSystem.DIRECTORY_INDEX_GENERAL+user.getUUID()+"/");
        lucene.initConfig(true);
        lucene.createIndex(user.basePathJsonFiles());
    }

    @Test
    void createIndexTwoDirectory() throws ParserConfigurationException, SAXException, IOException, SQLException {
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
    void searchIndex() throws IOException, ParseException, SQLException {
        initLucene();
        SearchResponse docs = lucene.searchIndex("weye\u0027 bajux");
        //List<Document> docs = lucene.searchIndex("tsíimino’ob");
    }

    @Test
    void searchPaginate() throws IOException, ParseException, SQLException {
        initLucene();
        ArrayList<SearchLuceneDoc> docs = lucene.searchPaginate("ma'", 1);
    }

    @Test
    void searchMutlipleIndex() throws IOException, ParseException, SQLException {
        //createIndex();
        Lucene lucene = new Lucene();
        lucene.searchMultipleIndex("ch´aalun",pathSystem.INDEX_LANGUAJE_MAYA, new ArrayList<>(), false);
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

    @Test
    void deleteIndex() throws IOException {
        File dir = new File(pathSystem.DIRECTORY_INDEX_GENERAL+"maya/04_006_05112022_05_MCC_E_2_1_1672857256919");
        FileUtils.deleteDirectory(dir.getCanonicalFile());
    }

    @Test
    void search_group() throws IOException, ParseException {
        //https://lucene.apache.org/core/9_1_0/core/org/apache/lucene/geo/LatLonGeometry.html
        List<IndexReader> indexReaders = new ArrayList<>();
        String getIndex = "español";

        File dir = new File(pathSystem.DIRECTORY_INDEX_GENERAL);
        File[] files = dir.listFiles();
        for (File file : files) {
            if(file.getName().equals(getIndex) && file.isDirectory()){
                File[] list_files = file.listFiles();
                for (File aux_file : list_files) {
                    if(file.isDirectory()){
                        Directory directory = FSDirectory.open(Paths.get(aux_file.getCanonicalPath()));
                        //Condición para no tomar los directorios que estan agregando al momento
                        if(DirectoryReader.indexExists(directory)){
                            indexReaders.add(DirectoryReader.open(directory));
                        }
                    }
                }
            }
        }

        MultiReader multiReader = new MultiReader(indexReaders.toArray(new IndexReader[indexReaders.size()]));
        IndexSearcher indexSearcher = new IndexSearcher(multiReader);

        GroupingSearch groupingSearch = new GroupingSearch("cvegeo");
        Sort groupSort = new Sort(new SortField("cvegeo", SortField.Type.STRING, true));  // in descending order
        groupingSearch.setGroupSort(groupSort);
        groupingSearch.setSortWithinGroup(groupSort);
        groupingSearch.setAllGroups(true);

        int offset = 0;
        int limitGroup = 2000;

        TermQuery query = new TermQuery(new Term("contents", "a"));
        TopGroups groups = groupingSearch.search(indexSearcher, query, offset, limitGroup);
        System.out.println("numero de documentos agrupados: " + groups.totalGroupedHitCount);
        System.out.println("numero de documentos en la busqueda: " + groups.totalHitCount);
        System.out.println("numero de grupos unicos: " + groups.totalGroupCount);
        System.out.println("Number matching Groups: " + groupingSearch.getAllMatchingGroups().size());

        List<Document> result = new ArrayList();
        for (int i=0; i<groups.groups.length; i++) {
            for (int j=0; j<groups.groups[i].scoreDocs.length; j++) {
                ScoreDoc sdoc = groups.groups[i].scoreDocs[j]; // first result of each group
                Document d = indexSearcher.doc(sdoc.doc);
                System.out.println("data "+ d.get("cvegeo"));
                System.out.println("data "+ d.get("timer_value_1"));
                System.out.println("data "+ d.get("timer_value_2"));
                System.out.println("data "+ d.get("original_multimedia"));
                System.out.println("data "+ d.get("multimedia"));
                System.out.println("data "+ d.get("project"));
            }
        }
    }
}