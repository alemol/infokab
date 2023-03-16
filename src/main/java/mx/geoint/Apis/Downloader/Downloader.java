package mx.geoint.Apis.Downloader;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.opencsv.CSVWriter;
import mx.geoint.Apis.Email.Emailer;
import mx.geoint.Controllers.Lucene.Lucene;
import mx.geoint.Model.Download.DownloadRequest;
import mx.geoint.Model.Search.SearchLuceneDoc;
import mx.geoint.Model.Search.SearchResponse;
import mx.geoint.pathSystem;
import org.apache.lucene.queryparser.classic.ParseException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class Downloader {

    private final Lucene lucene;
    private final Emailer emailer;

    public Downloader() {
        this.lucene = new Lucene();
        this.emailer = new Emailer();
    }

    public void prepare(DownloadRequest downloadRequest) throws IOException, ParseException, SQLException {
        SearchResponse response = lucene.searchMultipleIndex(downloadRequest.getText(), downloadRequest.getIndex(), false);
        ArrayList<SearchLuceneDoc> documents = response.getDocuments();
        int pages = (int)response.getTotalHits() / 10 + ((response.getTotalHits() % 10 == 0) ? 0 : 1);

        for (int i = 2; i <= pages; i++) {
            ArrayList<SearchLuceneDoc> pageDocs = lucene.searchPaginateMultiple(downloadRequest.getText(), i, downloadRequest.getIndex(), false);
            documents.addAll(pageDocs);
        }

        String csvFileName = createCSVFile(documents);

        zipFiles(documents, csvFileName);
    }

    public String createCSVFile(ArrayList<SearchLuceneDoc> documents) throws IOException{
        String directory_csv = existDirectory(pathSystem.DIRECTORY_CSV);
        List<String[]> csvData = buildCSVFile(documents);
        String fileName = NanoIdUtils.randomNanoId() + "_" + new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + ".csv";

        try(CSVWriter writer = new CSVWriter(new FileWriter(directory_csv + fileName))){
            writer.writeAll(csvData);
        }

        return fileName;
    }

    public List<String[]> buildCSVFile(ArrayList<SearchLuceneDoc> documents) {
        String[] header = {"idProyecto", "idSegmento", "texto", "ruta"};
        List<String[]> data = new ArrayList<>();

        data.add(header);

        JSONParser jsonParser = new JSONParser();

        try{
            for(SearchLuceneDoc document: documents){
                FileReader reader = new FileReader(document.getFilePath());
                Object obj = jsonParser.parse(reader);

                String[] record = {(String)((JSONObject) obj).get("PROJECT_NAME"), (String)((JSONObject) obj).get("REF_ANNOTATION_ID_TRANSCRIPCION_ORTOGRAFICA"),
                        (String)((JSONObject) obj).get("ANNOTATION_VALUE_TRANSCRIPCION_ORTOGRAFICA"), (String)((JSONObject) obj).get("MEDIA_PATH")};

                data.add(record);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        return data;
    }

    public void zipFiles(ArrayList<SearchLuceneDoc> documents, String csvFileName) throws IOException{

        String directory_download = existDirectory(pathSystem.DIRECTORY_DOWNLOADS);
        String directory_csv = existDirectory(pathSystem.DIRECTORY_CSV);

        FileOutputStream fos = new FileOutputStream(directory_download + NanoIdUtils.randomNanoId() + "_" + new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + ".zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for(SearchLuceneDoc document : documents){
            File fileToZip = new File("./Files" + document.getBasePath() + document.getMultimediaName() + ".wav");
            writeFileToZip(fileToZip, zipOut);
        }

        File csvFile = new File(directory_csv + csvFileName);
        writeFileToZip(csvFile, zipOut);

        zipOut.close();
        fos.close();

        emailer.sendEmail();
    }

    public void writeFileToZip(File fileToZip, ZipOutputStream zipOut) throws IOException{
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
        zipOut.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0){
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    private String existDirectory(String pathDirectory){
        String currentDirectory = pathDirectory;

        if(!Files.exists(Path.of(currentDirectory))){
            File newSubDirectory = new File(currentDirectory);
            newSubDirectory.mkdirs();
        }

        return currentDirectory;
    }
}
