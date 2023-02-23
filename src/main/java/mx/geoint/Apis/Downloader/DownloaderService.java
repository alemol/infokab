package mx.geoint.Apis.Downloader;

import mx.geoint.Model.Download.DownloadRequest;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;

@Service
public class DownloaderService {

    @Autowired Downloader downloader;

    public void prepareDownload(DownloadRequest downloadRequest) throws IOException, ParseException, SQLException {
        downloader.prepare(downloadRequest);
    }
}
