package mx.geoint.Controllers.Lucene;

import com.google.gson.Gson;
import mx.geoint.Model.ParseXML.TierMultiple;
import mx.geoint.Model.Project.ProjectPostgresLocations;
import mx.geoint.Model.Search.SearchLuceneDoc;
import mx.geoint.Model.Search.SearchResponse;
import mx.geoint.Model.ParseXML.Tier;
import mx.geoint.pathSystem;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.grouping.GroupingSearch;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static mx.geoint.Apis.Lucene.LuceneService.dbProjects;

public class Lucene {
    public String INDEX_DIRECTORY;

    public static final String FIELD_PATH = "path";
    public static final String FIELD_NAME = "filename";
    public static final String FIELD_CONTENTS = "contents";
    public static final String FIELD_VIEW = "view";
    public static final String FIELD_PATH_MULTIMEDIA = "multimedia";
    public static final String FIELD_PROJECT = "project";
    public static final String FIELD_CVEGEO = "cvegeo";
    public static final String FIELD_TIME_VALUE_1 = "timer_value_1";
    public static final String FIELD_TIME_VALUE_2 = "timer_value_2";
    public static final String FIELD_FULL_PATH_MULTIMEDIA = "original_multimedia";



    private static final int MAX_RESULTS = 9999;

    private Analyzer analyzer;
    private IndexWriterConfig config;
    private Directory indexDirectory;
    private IndexWriter indexWriter;

    public Lucene() {
        INDEX_DIRECTORY = pathSystem.DIRECTORY_INDEX_LUCENE;
    }

    public Lucene(String path) {
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

        if (create) {
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        } else {
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
        if (indexWriter == null || !indexWriter.isOpen()) {
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
        if(Files.exists(Path.of(path_files_to_index_directory))) {
            File dir = new File(path_files_to_index_directory);
            File[] files = dir.listFiles();

            for (File file : files) {
                Document document = new Document();

                String path = file.getPath();
                document.add(new StoredField(FIELD_PATH, path));

                String name = file.getName();
                document.add(new StoredField(FIELD_NAME, name));

                FileReader reader = new FileReader(file);
                Gson gson = new Gson();
                Tier tier = gson.fromJson(reader, Tier.class);
                document.add(new StoredField(FIELD_PATH_MULTIMEDIA, tier.MEDIA_PATH));
                document.add(new StoredField(FIELD_FULL_PATH_MULTIMEDIA, tier.ORIGINAL_MEDIA_PATH));
                document.add(new StoredField(FIELD_TIME_VALUE_1, tier.TIME_VALUE1));
                document.add(new StoredField(FIELD_TIME_VALUE_2, tier.TIME_VALUE2));

                document.add(new TextField(FIELD_CONTENTS, tier.ANNOTATION_VALUE, Field.Store.YES));

                document.add(new StringField(FIELD_CVEGEO, tier.CVEGEO, Field.Store.YES));
                document.add(new SortedDocValuesField(FIELD_CVEGEO, new BytesRef(tier.CVEGEO) ));
                document.add(new StoredField(FIELD_CVEGEO, tier.CVEGEO));

                document.add(new TextField(FIELD_PROJECT, tier.PROJECT_NAME, Field.Store.YES));
                document.add(new SortedDocValuesField(FIELD_PROJECT, new BytesRef(tier.PROJECT_NAME) ));
                document.add(new StoredField(FIELD_PROJECT, tier.PROJECT_NAME));


                indexWriter.addDocument(document);
            }
        }

        indexWriter.close();
    }

    public void createIndex(String path_files_to_index_directory, String index) throws IOException {
        if(Files.exists(Path.of(path_files_to_index_directory))) {
            File dir = new File(path_files_to_index_directory);
            File[] files = dir.listFiles();

            for (File file : files) {
                Document document = new Document();

                if(index.equals(pathSystem.TIER_GlOSA_INDEX)){
                    String path = file.getPath();
                    document.add(new StoredField(FIELD_PATH, path));

                    String name = file.getName();
                    document.add(new StoredField(FIELD_NAME, name));

                    FileReader reader = new FileReader(file);
                    Gson gson = new Gson();
                    TierMultiple tier = gson.fromJson(reader, TierMultiple.class);
                    document.add(new StoredField(FIELD_PATH_MULTIMEDIA, tier.MEDIA_PATH));
                    document.add(new StoredField(FIELD_FULL_PATH_MULTIMEDIA, tier.ORIGINAL_MEDIA_PATH));
                    document.add(new StoredField(FIELD_TIME_VALUE_1, tier.TIME_VALUE1));
                    document.add(new StoredField(FIELD_TIME_VALUE_2, tier.TIME_VALUE2));

                    document.add(new TextField(FIELD_CONTENTS, tier.ANNOTATION_VALUE_GLOSA_INDEX, Field.Store.YES));
                    document.add(new TextField(FIELD_VIEW, tier.ANNOTATION_VALUE_GLOSA_INDEX_WORDS, Field.Store.YES));

                    document.add(new StringField(FIELD_CVEGEO, tier.CVEGEO, Field.Store.YES));
                    document.add(new SortedDocValuesField(FIELD_CVEGEO, new BytesRef(tier.CVEGEO) ));
                    document.add(new StoredField(FIELD_CVEGEO, tier.CVEGEO));

                    document.add(new TextField(FIELD_PROJECT, tier.PROJECT_NAME, Field.Store.YES));
                    document.add(new SortedDocValuesField(FIELD_PROJECT, new BytesRef(tier.PROJECT_NAME) ));
                    document.add(new StoredField(FIELD_PROJECT, tier.PROJECT_NAME));

                }

                if(index.equals(pathSystem.TIER_TRANSLATE)){
                    String path = file.getPath();
                    document.add(new StoredField(FIELD_PATH, path));

                    String name = file.getName();
                    document.add(new StoredField(FIELD_NAME, name));

                    FileReader reader = new FileReader(file);
                    Gson gson = new Gson();
                    TierMultiple tier = gson.fromJson(reader, TierMultiple.class);

                    document.add(new StoredField(FIELD_PATH_MULTIMEDIA, tier.MEDIA_PATH));
                    document.add(new StoredField(FIELD_FULL_PATH_MULTIMEDIA, tier.ORIGINAL_MEDIA_PATH));
                    document.add(new StoredField(FIELD_TIME_VALUE_1, tier.TIME_VALUE1));
                    document.add(new StoredField(FIELD_TIME_VALUE_2, tier.TIME_VALUE2));

                    document.add(new TextField(FIELD_CONTENTS, tier.ANNOTATION_VALUE_TRADUCCION_LIBRE, Field.Store.YES));
                    document.add(new TextField(FIELD_VIEW, tier.ANNOTATION_VALUE_TRANSCRIPCION_ORTOGRAFICA, Field.Store.YES));

                    document.add(new StringField(FIELD_CVEGEO, tier.CVEGEO, Field.Store.YES));
                    document.add(new SortedDocValuesField(FIELD_CVEGEO, new BytesRef(tier.CVEGEO) ));
                    document.add(new StoredField(FIELD_CVEGEO, tier.CVEGEO));

                    document.add(new TextField(FIELD_PROJECT, tier.PROJECT_NAME, Field.Store.YES));
                    document.add(new SortedDocValuesField(FIELD_PROJECT, new BytesRef(tier.PROJECT_NAME) ));
                    document.add(new StoredField(FIELD_PROJECT, tier.PROJECT_NAME));
                }

                indexWriter.addDocument(document);
            }
        }

        indexWriter.close();
    }

    /*
     * Realizar una busqueda para un directorio en especifico (Verificar si se elimina)
     * @param searchString Texto a buscar en los indices
     * @return List<Document> lista de documentos encontrados
     **/
    public SearchResponse searchIndex(String searchString) throws IOException, ParseException, SQLException {
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

        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            int docId = scoreDoc.doc;
            float docScore = scoreDoc.score;
            Document hitDoc = indexSearcher.doc(docId);
            System.out.println("doc=" + docId + " score=" + docScore + " path=" + hitDoc.get(FIELD_PATH));

            String path = hitDoc.get("path");
            String fileName = hitDoc.get("filename");
            String content = hitDoc.get("contents");
            String multimedia = hitDoc.get("multimedia");

            String[] imageList = find_images(hitDoc.get("path"));
            String[] videoList = find_videos(hitDoc.get("path"));
            String fecha_archivo = null, entidad = null, municipio = null, Nhablantes = null, localidad = null, coordinates = null, bbox = null;
            String[] dbResponse = dbProjects.getProjectByName(path.split("/")[4]);
            fecha_archivo = dbResponse[0];
            Nhablantes = dbResponse[1];
            entidad = dbResponse[2];
            municipio = dbResponse[3];
            localidad = dbResponse[4];
            coordinates = dbResponse[5];
            bbox = dbResponse[6];
            SearchLuceneDoc doc = new SearchLuceneDoc(path, fileName, content, docScore, imageList, videoList, fecha_archivo, Nhablantes, entidad, municipio, localidad, coordinates, bbox, multimedia);
            results.add(doc);
        }

        SearchResponse searchResponse = new SearchResponse(results, hits.totalHits.value);

        indexReader.close();
        directory.close();
        return searchResponse;
    }

    public ArrayList<SearchLuceneDoc> searchPaginate(String search, int page) throws IOException, ParseException, SQLException {
        System.out.println("pagina=" + page);
        Analyzer analyzer = new StandardAnalyzer();
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        DirectoryReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        TopScoreDocCollector collector = TopScoreDocCollector.create(MAX_RESULTS, 10);
        int startIndex = (page - 1) * 10;
        QueryParser queryParser = new QueryParser(FIELD_CONTENTS, analyzer);
        Query new_query = queryParser.parse(search);
        indexSearcher.search(new_query, collector);

        TopDocs hits = collector.topDocs(startIndex, 10);
        System.out.println("totalHits: " + hits.totalHits);

        ArrayList<SearchLuceneDoc> results = new ArrayList<SearchLuceneDoc>();

        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            int docId = scoreDoc.doc;
            float docScore = scoreDoc.score;
            Document hitDoc = indexReader.document(docId);
            System.out.println("doc=" + docId + " score=" + docScore + " path=" + hitDoc.get(FIELD_PATH));

            String path = hitDoc.get("path");
            String fileName = hitDoc.get("filename");
            String content = hitDoc.get("contents");
            String multimedia = hitDoc.get("multimedia");

            String[] imageList = find_images(hitDoc.get("path"));
            String[] videoList = find_videos(hitDoc.get("path"));
            String fecha_archivo = null, entidad = null, municipio = null, Nhablantes = null, localidad = null, coordinates = null, bbox = null;
            String[] dbResponse = dbProjects.getProjectByName(path.split("/")[4]);
            fecha_archivo = dbResponse[0];
            Nhablantes = dbResponse[1];
            entidad = dbResponse[2];
            municipio = dbResponse[3];
            localidad = dbResponse[4];
            coordinates = dbResponse[5];
            bbox = dbResponse[6];
            SearchLuceneDoc doc = new SearchLuceneDoc(path, fileName, content, docScore, imageList, videoList, fecha_archivo, Nhablantes, entidad, municipio, localidad, coordinates, bbox, multimedia);
            results.add(doc);
        }

        indexReader.close();
        directory.close();
        return results;
    }

    /*
     * Realiza la busqueda de un texto en los indices de la caperta raiz DIRECTORY_INDEX_GENERAL
     * @param search texto a buscar
     * @return List<Document> Lista de documentos encontrados
     **/
    public SearchResponse searchMultipleIndex(String search, String index, ArrayList<String> cvegeo, boolean levenshtein) throws IOException, ParseException, SQLException {
        List<IndexReader> indexReaders = new ArrayList<>();
        Analyzer analyzer = new StandardAnalyzer();
        //System.out.println("Searching for '" + search + "'");
        String getIndex = index;
        if(index.equals("maya")){
            getIndex = "español";
        }

        //Se Obtiene todos los indices generados en la caperta DIRECTORY_INDEX_GENERAL
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

        //Creacion de indexSearch con muchos indices
        MultiReader multiReader = new MultiReader(indexReaders.toArray(new IndexReader[indexReaders.size()]));
        IndexSearcher indexSearcher = new IndexSearcher(multiReader);

        QueryParser queryParser = new QueryParser(FIELD_CONTENTS, analyzer);
        Query new_query = null;
        String query_cvegeo = "";
        if(levenshtein){
            new_query = new FuzzyQuery(new Term(FIELD_CONTENTS, search));
        } else {
            if(cvegeo != null && !cvegeo.isEmpty()) {
                String result_cvegeo = String.join(" ", cvegeo);
                query_cvegeo = " AND " +FIELD_CVEGEO + ":" + "(" + result_cvegeo + ")";
            }

            if(index.equals("glosado")) {
                String combinate_searchString = FIELD_CONTENTS + ":" + search + " OR " + FIELD_VIEW + ":" + search + query_cvegeo;
                new_query = queryParser.parse(combinate_searchString);
            } else if(index.equals("maya")){
                String combinate_searchString = FIELD_VIEW + ":" + search + query_cvegeo;
                new_query = queryParser.parse(combinate_searchString);
            }else{
                String combinate_searchString = FIELD_CONTENTS + ":" + search + query_cvegeo;
                new_query = queryParser.parse(combinate_searchString);
            }
        }
        TopDocs hits = indexSearcher.search(new_query, 10);
        System.out.println("totalHits: " + hits.totalHits);
        //Obtención de información de los documentos encontrados
        ArrayList<SearchLuceneDoc> results = new ArrayList<SearchLuceneDoc>();

        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            int docId = scoreDoc.doc;
            float docScore = scoreDoc.score;

            Document hitDoc = indexSearcher.doc(docId);
            System.out.println("doc=" + docId + " score=" + docScore + " path=" + hitDoc.get(FIELD_PATH));

            String path = hitDoc.get(FIELD_PATH);
            String fileName = hitDoc.get(FIELD_NAME);
            String multimedia = hitDoc.get(FIELD_PATH_MULTIMEDIA);
            String content = hitDoc.get(FIELD_VIEW);
            String subText = hitDoc.get(FIELD_CONTENTS);

            String[] imageList = find_images(hitDoc.get(FIELD_PATH));
            String[] videoList = find_videos(hitDoc.get(FIELD_PATH));
            String fecha_archivo = null, entidad = null, municipio = null, Nhablantes = null, localidad = null, coordinates = null, bbox = null;
            String[] dbResponse = dbProjects.getProjectByName(path.split("/")[4]);
            fecha_archivo = dbResponse[0];
            Nhablantes = dbResponse[1];
            entidad = dbResponse[2];
            municipio = dbResponse[3];
            localidad = dbResponse[4];
            coordinates = dbResponse[5];
            bbox = dbResponse[6];
            SearchLuceneDoc doc = new SearchLuceneDoc(path, fileName, content, docScore, imageList, videoList, fecha_archivo, Nhablantes, entidad, municipio, localidad, coordinates, bbox, multimedia);
            doc.setSubText(subText);
            results.add(doc);
        }

        SearchResponse searchResponse = new SearchResponse(results, hits.totalHits.value);

        multiReader.close();
        return searchResponse;
    }

    public ArrayList<SearchLuceneDoc> searchPaginateMultiple(String search, int page, String index, ArrayList<String> cvegeo, boolean levenshtein) throws IOException, ParseException, SQLException {
        List<IndexReader> indexReaders = new ArrayList<>();

        Analyzer analyzer = new StandardAnalyzer();

        String getIndex = index;
        if(index.equals("maya")){
            getIndex = "español";
        }

        //Se Obtiene todos los indices generados en la caperta DIRECTORY_INDEX_GENERAL
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

        //Creacion de indexSearch con muchos indices
        MultiReader multiReader = new MultiReader(indexReaders.toArray(new IndexReader[indexReaders.size()]));
        IndexSearcher indexSearcher = new IndexSearcher(multiReader);

        TopScoreDocCollector collector = TopScoreDocCollector.create(MAX_RESULTS, 10);
        int startIndex = (page - 1) * 10;
        QueryParser queryParser = new QueryParser(FIELD_CONTENTS, analyzer);
        Query new_query = null;
        String query_cvegeo = "";

        if(levenshtein){
            new_query = new FuzzyQuery(new Term(FIELD_CONTENTS, search));
        } else {
            if(cvegeo != null && !cvegeo.isEmpty()) {
                String result_cvegeo = String.join(" ", cvegeo);
                query_cvegeo = " AND " +FIELD_CVEGEO + ":" + "(" + result_cvegeo + ")";
            }

            if(index.equals("glosado")) {
                String combinate_searchString = FIELD_CONTENTS + ":" + search + " OR " + FIELD_VIEW + ":" + search + query_cvegeo;
                new_query = queryParser.parse(combinate_searchString);
            } else if(index.equals("maya")){
                String combinate_searchString = FIELD_VIEW + ":" + search + query_cvegeo;
                new_query = queryParser.parse(combinate_searchString);
            }else{
                String combinate_searchString = FIELD_CONTENTS + ":" + search + query_cvegeo;
                new_query = queryParser.parse(combinate_searchString);
            }
        }

        indexSearcher.search(new_query, collector);

        TopDocs hits = collector.topDocs(startIndex, 10);
        System.out.println("totalHits: " + hits.totalHits);

        //Obtención de información de los documentos encontrados
        ArrayList<SearchLuceneDoc> results = new ArrayList<SearchLuceneDoc>();

        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            int docId = scoreDoc.doc;
            float docScore = scoreDoc.score;

            Document hitDoc = indexSearcher.doc(docId);
            System.out.println("doc=" + docId + " score=" + docScore + " path=" + hitDoc.get(FIELD_PATH));

            String path = hitDoc.get(FIELD_PATH);
            String fileName = hitDoc.get(FIELD_NAME);
            String multimedia = hitDoc.get(FIELD_PATH_MULTIMEDIA);

            String content = hitDoc.get(FIELD_VIEW);
            String subText = hitDoc.get(FIELD_CONTENTS);

            String[] imageList = find_images(hitDoc.get("path"));
            String[] videoList = find_videos(hitDoc.get("path"));
            String fecha_archivo = null, entidad = null, municipio = null, Nhablantes = null, localidad = null, coordinates = null, bbox = null;
            String[] dbResponse = dbProjects.getProjectByName(path.split("/")[4]);
            fecha_archivo = dbResponse[0];
            Nhablantes = dbResponse[1];
            entidad = dbResponse[2];
            municipio = dbResponse[3];
            localidad = dbResponse[4];
            coordinates = dbResponse[5];
            bbox = dbResponse[6];
            SearchLuceneDoc doc = new SearchLuceneDoc(path, fileName, content, docScore, imageList, videoList, fecha_archivo, Nhablantes, entidad, municipio, localidad, coordinates, bbox, multimedia);
            doc.setSubText(subText);
            results.add(doc);
        }

        multiReader.close();
        return results;
    }

    public ArrayList<ProjectPostgresLocations> searchMultipleLocations(String search, String index) throws IOException, SQLException {
        //https://lucene.apache.org/core/9_1_0/core/org/apache/lucene/geo/LatLonGeometry.html
        List<IndexReader> indexReaders = new ArrayList<>();
        String getIndex = index;
        if (index.equals("maya")) {
            getIndex = "español";
        }

        File dir = new File(pathSystem.DIRECTORY_INDEX_GENERAL);
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.getName().equals(getIndex) && file.isDirectory()) {
                File[] list_files = file.listFiles();
                for (File aux_file : list_files) {
                    if (file.isDirectory()) {
                        Directory directory = FSDirectory.open(Paths.get(aux_file.getCanonicalPath()));
                        //Condición para no tomar los directorios que estan agregando al momento
                        if (DirectoryReader.indexExists(directory)) {
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

        String field = FIELD_CONTENTS;
        if (index.equals("maya")) {
            field = FIELD_VIEW;
        }

        TopGroups groups;
        if (index.equals("glosado")) {
            TermQuery query = new TermQuery(new Term(FIELD_CONTENTS, search));
            TermQuery query2 = new TermQuery(new Term(FIELD_VIEW, search));

            BooleanQuery booleanQuery = new BooleanQuery.Builder()
                    .add(query, BooleanClause.Occur.SHOULD)
                    .add(query2, BooleanClause.Occur.SHOULD)
                    .build();

            groups = groupingSearch.search(indexSearcher, booleanQuery, offset, limitGroup);
        }else{
            TermQuery query = new TermQuery(new Term(field, search));
            groups = groupingSearch.search(indexSearcher, query, offset, limitGroup);
        }

        String[] list_cvegeo = new String[groups.groups.length];
        for (int i = 0; i < groups.groups.length; i++) {
            for (int j = 0; j < groups.groups[i].scoreDocs.length; j++) {
                ScoreDoc sdoc = groups.groups[i].scoreDocs[j]; // first result of each group
                Document d = indexSearcher.doc(sdoc.doc);
                list_cvegeo[i] = d.get("cvegeo");
                System.out.println("data " + d.get("cvegeo"));
            }
        }

        ArrayList<ProjectPostgresLocations> projectPostgresLocations = dbProjects.getLocations(list_cvegeo);
        return projectPostgresLocations;
    }

    public String[] find_images(String path) {
        System.out.println("path:" + path);
        String[] arrOfStr = path.split("(?:maya|español|glosado)");
        System.out.println("path:" + arrOfStr.toString());
        String imagesDir = arrOfStr[0] + "Images/";
        String[] imageList = null;

        if (Files.exists(Path.of(imagesDir))) {
            String[] pathnames;
            File f = new File(imagesDir);
            pathnames = f.list();
            if (pathnames.length > 0) {
                imageList = pathnames;
            } else {
                imageList = null;
            }
        }
        return imageList;
    }

    public String[] find_videos(String path) {
        String[] arrOfStr = path.split("(?:maya|español|glosado)");
        String videosDir = arrOfStr[0] + "Video/";
        String[] videoList = null;

        if (Files.exists(Path.of(videosDir))) {
            String[] pathnames;
            File f = new File(videosDir);
            pathnames = f.list();
            if (pathnames.length > 0) {
                videoList = pathnames;
            } else {
                videoList = null;
            }
        }
        return videoList;
    }
}

