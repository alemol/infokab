package mx.geoint.Lucene;

import com.google.gson.Gson;
import mx.geoint.Model.SearchDoc;
import mx.geoint.Response.SearchResponse;
import mx.geoint.ParseXML.Tier;
import mx.geoint.pathSystem;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Lucene{
    public static final String INDEX_DIRECTORY = pathSystem.DIRECTORY_INDEX_LUCENE;

    public static final String FIELD_PATH = "path";
    public static final String FIELD_NAME = "filename";
    public static final String FIELD_CONTENTS = "contents";

    private static final int MAX_RESULTS = 9999;

    public Lucene(){}


    public static void createIndex(String path_files_to_index_directory, boolean create) throws IOException {
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        if(create){
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        }else{
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        }

        Directory indexDirectory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        IndexWriter indexWriter = new IndexWriter(indexDirectory, config);

        if(indexWriter == null){
            throw new IllegalArgumentException("IndexWrite instance must not be null");
        }

        if(!indexWriter.isOpen()){
            throw new IllegalArgumentException("IndexWrite instance must be open");
        }

        File dir = new File(path_files_to_index_directory);
        File[] files = dir.listFiles();
        for (File file : files) {
            Document document = new Document();

            String path = file.getCanonicalPath();
            document.add(new StringField(FIELD_PATH, path, Field.Store.YES));

            String name = file.getName();
            document.add(new StringField(FIELD_NAME, name, Field.Store.YES));

            //String reader = new String(Files.readAllBytes(Paths.get(String.valueOf(file))));
            //document.add(new TextField(FIELD_CONTENTS, reader, Field.Store.YES));
            FileReader reader = new FileReader(file);
            Gson gson = new Gson();
            Tier tier = gson.fromJson(reader, Tier.class);
            System.out.println("Value: "+ tier.ANNOTATION_VALUE);
            document.add(new TextField(FIELD_CONTENTS, tier.ANNOTATION_VALUE, Field.Store.YES));

            indexWriter.addDocument(document);
        }

        indexWriter.close();
    }

    public static SearchResponse searchIndex(String searchString) throws IOException, ParseException {
        Analyzer analyzer = new StandardAnalyzer();
        System.out.println("Searching for '" + searchString + "'");
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        DirectoryReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        QueryParser queryParser = new QueryParser(FIELD_CONTENTS, analyzer);
        Query new_query = queryParser.parse(searchString);
        TopDocs hits = indexSearcher.search(new_query, 10);
        System.out.println("totalHits: " + hits.totalHits);

        ArrayList<SearchDoc> results = new ArrayList<SearchDoc>();

        for (ScoreDoc scoreDoc: hits.scoreDocs) {
            int docId = scoreDoc.doc;
            float docScore = scoreDoc.score;
            Document hitDoc = indexReader.document(docId);
            System.out.println("doc="+docId +" score=" + docScore +" path="+ hitDoc.get(FIELD_PATH));

            String path = hitDoc.get("path");
            String fileName = hitDoc.get("filename");
            String content = hitDoc.get("contents");


            SearchDoc doc = new SearchDoc(path, fileName, content, docScore);
            results.add(doc);
        }

        SearchResponse searchResponse = new SearchResponse(results, hits.totalHits.value);

        indexReader.close();
        directory.close();
        return searchResponse;
    }

    public ArrayList<SearchDoc> searchPaginate(String search, int page) throws IOException, ParseException {
        System.out.println("pagina="+ page);
        Analyzer analyzer = new StandardAnalyzer();
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        DirectoryReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        TopScoreDocCollector collector = TopScoreDocCollector.create(MAX_RESULTS, 10);
        int startIndex = (page -1) * 10;
        QueryParser queryParser = new QueryParser(FIELD_CONTENTS, analyzer);
        Query new_query = queryParser.parse(search);
        indexSearcher.search(new_query, collector);

        TopDocs hits = collector.topDocs(startIndex, 10);
        System.out.println("totalHits: " + hits.totalHits);

        ArrayList<SearchDoc> results = new ArrayList<SearchDoc>();

        for (ScoreDoc scoreDoc: hits.scoreDocs) {
            int docId = scoreDoc.doc;
            float docScore = scoreDoc.score;
            Document hitDoc = indexReader.document(docId);
            System.out.println("doc="+docId +" score=" + docScore +" path="+ hitDoc.get(FIELD_PATH));

            String path = hitDoc.get("path");
            String fileName = hitDoc.get("filename");
            String content = hitDoc.get("contents");


            SearchDoc doc = new SearchDoc(path, fileName, content, docScore);
            results.add(doc);
        }

        indexReader.close();
        directory.close();
        return results;
    }
}

