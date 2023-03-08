package mx.geoint.Apis.Downloader;

import mx.geoint.Model.Download.DownloadRequest;
import mx.geoint.pathSystem;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class DownloaderService {

    @Autowired Downloader downloader;

    public void prepareDownload(DownloadRequest downloadRequest) throws IOException, ParseException, SQLException {
        downloader.prepare(downloadRequest);
    }


    public ArrayList<String> getLoggerFiles() {
        File dir = new File(pathSystem.DIRECTORY_DOWNLOADS);
        File[] files = dir.listFiles();
        ArrayList<String> listName = new ArrayList<>();

        for (File file : files) {
            listName.add(file.getName());
        }
        return listName;
    }
}
