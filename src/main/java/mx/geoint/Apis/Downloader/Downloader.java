package mx.geoint.Apis.Downloader;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import mx.geoint.Controllers.Lucene.Lucene;
import mx.geoint.Model.Download.DownloadRequest;
import mx.geoint.Model.Search.SearchLuceneDoc;
import mx.geoint.Model.Search.SearchResponse;
import mx.geoint.pathSystem;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class Downloader {

    private final Lucene lucene;

    public Downloader() {
        this.lucene = new Lucene();
    }

    public void prepare(DownloadRequest downloadRequest) throws IOException, ParseException, SQLException {
        SearchResponse response = lucene.searchMultipleIndex(downloadRequest.getText(), downloadRequest.getIndex(), false);
        ArrayList<SearchLuceneDoc> documents = response.getDocuments();
        int pages = (int)response.getTotalHits() / 10 + ((response.getTotalHits() % 10 == 0) ? 0 : 1);

        for (int i = 2; i <= pages; i++) {
            ArrayList<SearchLuceneDoc> pageDocs = lucene.searchPaginateMultiple(downloadRequest.getText(), i, downloadRequest.getIndex(), false);
            documents.addAll(pageDocs);
        }

        zipFiles(documents);
    }

    public void zipFiles(ArrayList<SearchLuceneDoc> documents) throws IOException{
        FileOutputStream fos = new FileOutputStream(pathSystem.DIRECTORY_DOWNLOADS + NanoIdUtils.randomNanoId() + ".zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for(SearchLuceneDoc document : documents){
            File fileToZip = new File("./Files" + document.getBasePath() + document.getMultimediaName() + ".wav");
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

        zipOut.close();
        fos.close();
    }
}
