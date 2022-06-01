package mx.geoint.Lucene;

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
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Lucene{
    public static final String INDEX_DIRECTORY = "/home/alejandro/Documentos/projects/infokab-backend/Files/Index";

    public static final String FIELD_PATH = "path";
    public static final String FIELD_NAME = "filename";
    public static final String FIELD_CONTENTS = "contents";

    Lucene(){}

    public static void createIndex(String path_files_to_index_directory) throws IOException {
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        Directory indexDirectory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        IndexWriter indexWriter = new IndexWriter(indexDirectory, config);

        File dir = new File(path_files_to_index_directory);
        File[] files = dir.listFiles();
        for (File file : files) {
            Document document = new Document();

            String path = file.getCanonicalPath();
            document.add(new StringField(FIELD_PATH, path, Field.Store.YES));

            String name = file.getName();
            document.add(new StringField(FIELD_NAME, name, Field.Store.YES));

            String reader = new String(Files.readAllBytes(Paths.get(String.valueOf(file))));
            document.add(new TextField(FIELD_CONTENTS, reader, Field.Store.YES));

            indexWriter.addDocument(document);
        }

        indexWriter.close();
    }

    public static List<Document> searchIndex(String searchString) throws IOException, ParseException {
        Analyzer analyzer = new StandardAnalyzer();
        System.out.println("Searching for '" + searchString + "'");
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        DirectoryReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        QueryParser queryParser = new QueryParser(FIELD_CONTENTS, analyzer);
        Query new_query = queryParser.parse(searchString);
        TopDocs hits = indexSearcher.search(new_query, 10);
        System.out.println("totalHits: " + hits.totalHits);

        List<Document> docs = new ArrayList<>();
        for (ScoreDoc scoreDoc: hits.scoreDocs) {
            int docId = scoreDoc.doc;
            float docScore = scoreDoc.score;
            Document hitDoc = indexReader.document(docId);
            System.out.println("doc="+docId +" score=" + docScore +" path="+ hitDoc.get(FIELD_PATH));
            docs.add(hitDoc);
        }

        indexReader.close();
        directory.close();
        return docs;
    }
}

