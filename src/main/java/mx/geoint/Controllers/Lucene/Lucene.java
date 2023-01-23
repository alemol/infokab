package mx.geoint.Controllers.Lucene;

import com.google.gson.Gson;
import mx.geoint.Model.Search.SearchLuceneDoc;
import mx.geoint.Model.Search.SearchResponse;
import mx.geoint.Model.ParseXML.Tier;
import mx.geoint.pathSystem;
import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Lucene{
    public String INDEX_DIRECTORY;

    public static final String FIELD_PATH = "path";
    public static final String FIELD_NAME = "filename";
    public static final String FIELD_CONTENTS = "contents";
    public static final String FIELD_PATH_MULTIMEDIA = "multimedia";

    private static final int MAX_RESULTS = 9999;

    private Analyzer analyzer;
    private IndexWriterConfig config;
    private Directory indexDirectory;
    private IndexWriter indexWriter;
    public Lucene(){
        INDEX_DIRECTORY = pathSystem.DIRECTORY_INDEX_LUCENE;
    }
    public Lucene(String path){
        INDEX_DIRECTORY = path;
    }

    /*
     * Inicializa el proceso para poder agregar nuevos documentos
     * @param create True si se hara de cero el indice, false para indexar nuevo documentos al indice existente
     * @return
     **/
    public void initConfig(boolean create) throws IOException {
        analyzer = new StandardAnalyzer();
        config = new IndexWriterConfig(analyzer);

        if(create){
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        }else{
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        }

        //System.out.println("directory"+ INDEX_DIRECTORY);
        indexDirectory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));

        statusIndexWrite();
    }

    /*
     * Inicializa el indexWrite si no existe, en caso contrario regresa el ya inicializado
     * @param
     * @return indexWrite para agregar documentos al indices
     **/
    public IndexWriter statusIndexWrite() throws IOException {
        if(indexWriter == null || !indexWriter.isOpen()){
            indexWriter = new IndexWriter(indexDirectory, config);
        }

        return indexWriter;
    }

    /*
     * Indexa los documentos del directory proporcionado
     * @param path_files_to_index_directory Path de los json a indexar
     * @return
     **/
    public void createIndex(String path_files_to_index_directory) throws IOException {
        File dir = new File(path_files_to_index_directory);
        File[] files = dir.listFiles();

        for (File file : files) {
            Document document = new Document();

            String path = file.getPath();
            document.add(new StringField(FIELD_PATH, path, Field.Store.YES));

            String name = file.getName();
            document.add(new StringField(FIELD_NAME, name, Field.Store.YES));

            FileReader reader = new FileReader(file);
            Gson gson = new Gson();
            Tier tier = gson.fromJson(reader, Tier.class);
            document.add(new StringField(FIELD_PATH_MULTIMEDIA, tier.MEDIA_PATH, Field.Store.YES));
            document.add(new TextField(FIELD_CONTENTS, tier.ANNOTATION_VALUE, Field.Store.YES));

            indexWriter.addDocument(document);
        }

        indexWriter.close();
    }

    /*
     * Realizar una busqueda para un directorio en especifico (Verificar si se elimina)
     * @param searchString Texto a buscar en los indices
     * @return List<Document> lista de documentos encontrados
     **/
    public SearchResponse searchIndex(String searchString) throws IOException, ParseException {
        Analyzer analyzer = new StandardAnalyzer();
        System.out.println("Searching for '" + searchString + "'");

        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        DirectoryReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        QueryParser queryParser = new QueryParser(FIELD_CONTENTS, analyzer);
        Query new_query = queryParser.parse(searchString);
        TopDocs hits = indexSearcher.search(new_query, 10);
        System.out.println("totalHits: " + hits.totalHits);

        ArrayList<SearchLuceneDoc> results = new ArrayList<SearchLuceneDoc>();

        for (ScoreDoc scoreDoc: hits.scoreDocs) {
            int docId = scoreDoc.doc;
            float docScore = scoreDoc.score;
            Document hitDoc = indexSearcher.doc(docId);
            System.out.println("doc="+docId +" score=" + docScore +" path="+ hitDoc.get(FIELD_PATH));

            String path = hitDoc.get("path");
            String fileName = hitDoc.get("filename");
            String content = hitDoc.get("contents");
            String multimedia = hitDoc.get("multimedia");

            String[] imageList = null;
            SearchLuceneDoc doc = new SearchLuceneDoc(path, fileName, content, docScore, multimedia, imageList);
            results.add(doc);
        }

        SearchResponse searchResponse = new SearchResponse(results, hits.totalHits.value);

        indexReader.close();
        directory.close();
        return searchResponse;
    }

    public ArrayList<SearchLuceneDoc> searchPaginate(String search, int page) throws IOException, ParseException {
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

        ArrayList<SearchLuceneDoc> results = new ArrayList<SearchLuceneDoc>();

        for (ScoreDoc scoreDoc: hits.scoreDocs) {
            int docId = scoreDoc.doc;
            float docScore = scoreDoc.score;
            Document hitDoc = indexReader.document(docId);
            System.out.println("doc="+docId +" score=" + docScore +" path="+ hitDoc.get(FIELD_PATH));

            String path = hitDoc.get("path");
            String fileName = hitDoc.get("filename");
            String content = hitDoc.get("contents");
            String multimedia = hitDoc.get("multimedia");

            String[] imageList = null;
            SearchLuceneDoc doc = new SearchLuceneDoc(path, fileName, content, docScore, multimedia, imageList);
            results.add(doc);
        }

        indexReader.close();
        directory.close();
        return results;
    }

    /*
     * Realiza la busqueda de un texto en los indices de la caperta raiz DIRECTORY_INDEX_GENERAL
     * @param searchString texto a buscar
     * @return List<Document> Lista de documentos encontrados
     **/
    public SearchResponse searchMultipleIndex(String searchString, String index) throws IOException, ParseException {
        List<IndexReader> indexReaders = new ArrayList<>();

        Analyzer analyzer = new StandardAnalyzer();
        //System.out.println("Searching for '" + searchString + "'");

        //Se Obtiene todos los indices generados en la caperta DIRECTORY_INDEX_GENERAL
        File dir = new File(pathSystem.DIRECTORY_INDEX_GENERAL);
        File[] files = dir.listFiles();
        for (File file : files) {
            if(file.getName().equals(index) && file.isDirectory()){
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

        //Creacion de indexSearch con muchos indices
        MultiReader multiReader = new MultiReader(indexReaders.toArray(new IndexReader[indexReaders.size()]));
        IndexSearcher indexSearcher = new IndexSearcher(multiReader);

        QueryParser queryParser = new QueryParser(FIELD_CONTENTS, analyzer);
        Query new_query = queryParser.parse(searchString);
        TopDocs hits = indexSearcher.search(new_query, 10);
        System.out.println("totalHits: " + hits.totalHits);

        //Obtención de información de los documentos encontrados
        ArrayList<SearchLuceneDoc> results = new ArrayList<SearchLuceneDoc>();

        for (ScoreDoc scoreDoc: hits.scoreDocs) {
            int docId = scoreDoc.doc;
            float docScore = scoreDoc.score;

            Document hitDoc = indexSearcher.doc(docId);
            System.out.println("doc="+docId +" score=" + docScore +" path="+ hitDoc.get(FIELD_PATH));

            String path = hitDoc.get("path");
            String fileName = hitDoc.get("filename");
            String content = hitDoc.get("contents");
            String multimedia = hitDoc.get("multimedia");

            //System.out.println("PATH: "+ hitDoc.get("path"));
            String[] arrOfStr = hitDoc.get("path").split("file_to_index");
            //System.out.println(arrOfStr[0]);
            String imagesDir = arrOfStr[0] + "Images/";
            String[] imageList = null ;

            if (Files.exists(Path.of(imagesDir))){
                String[] pathnames;

                File f = new File(imagesDir);

                pathnames = f.list();
                if(pathnames.length>0){
                    for (String pathname : pathnames) {
                        String x = FilenameUtils.getBaseName(pathname);
                    }
                    imageList=pathnames;

                }else{
                    imageList= null;
                }
            }



            SearchLuceneDoc doc = new SearchLuceneDoc(path, fileName, content, docScore, multimedia, imageList);

            results.add(doc);
        }

        SearchResponse searchResponse = new SearchResponse(results, hits.totalHits.value);

        multiReader.close();
        return searchResponse;
    }

    public ArrayList<SearchLuceneDoc> searchPaginateMultiple(String search, int page, String index) throws IOException, ParseException {
        List<IndexReader> indexReaders = new ArrayList<>();

        Analyzer analyzer = new StandardAnalyzer();
        //System.out.println("Searching for '" + searchString + "'");

        //Se Obtiene todos los indices generados en la caperta DIRECTORY_INDEX_GENERAL
        File dir = new File(pathSystem.DIRECTORY_INDEX_GENERAL);
        File[] files = dir.listFiles();
        for (File file : files) {
            if(file.getName().equals(index) && file.isDirectory()){
                File[] list_files = file.listFiles();
                for (File aux_file : list_files) {
                    if(file.isDirectory()) {
                        Directory directory = FSDirectory.open(Paths.get(aux_file.getCanonicalPath()));
                        //Condición para no tomar los directorios que estan agregando al momento
                        if(DirectoryReader.indexExists(directory)){
                            indexReaders.add(DirectoryReader.open(directory));
                        }
                    }
                }
            }
        }

        //Creacion de indexSearch con muchos indices
        MultiReader multiReader = new MultiReader(indexReaders.toArray(new IndexReader[indexReaders.size()]));
        IndexSearcher indexSearcher = new IndexSearcher(multiReader);

        TopScoreDocCollector collector = TopScoreDocCollector.create(MAX_RESULTS, 10);
        int startIndex = (page -1) * 10;
        QueryParser queryParser = new QueryParser(FIELD_CONTENTS, analyzer);
        Query new_query = queryParser.parse(search);
        indexSearcher.search(new_query, collector);

        TopDocs hits = collector.topDocs(startIndex, 10);
        System.out.println("totalHits: " + hits.totalHits);

        //Obtención de información de los documentos encontrados
        ArrayList<SearchLuceneDoc> results = new ArrayList<SearchLuceneDoc>();

        for (ScoreDoc scoreDoc: hits.scoreDocs) {
            int docId = scoreDoc.doc;
            float docScore = scoreDoc.score;

            Document hitDoc = indexSearcher.doc(docId);
            System.out.println("doc="+docId +" score=" + docScore +" path="+ hitDoc.get(FIELD_PATH));

            String path = hitDoc.get("path");
            String fileName = hitDoc.get("filename");
            String content = hitDoc.get("contents");
            String multimedia = hitDoc.get("multimedia");

            String[] imageList = null;
            SearchLuceneDoc doc = new SearchLuceneDoc(path, fileName, content, docScore, multimedia, imageList);
            results.add(doc);
        }

        multiReader.close();
        return results;
    }
}

