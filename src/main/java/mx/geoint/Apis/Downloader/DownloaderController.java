package mx.geoint.Apis.Downloader;

import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Model.Download.DownloadRequest;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.SQLException;

@CrossOrigin(origins = {"http://infokaab.com/","http://infokaab.com.mx/","http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009","http://10.2.102.182"})
@RestController
@RequestMapping(path = "api/download")
public class DownloaderController {

    private final DownloaderService downloaderService;

    private Logger logger;

    @Autowired
    public DownloaderController(DownloaderService downloaderService){
        this.downloaderService = downloaderService;
        this.logger = new Logger();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> downloadResult(@RequestBody DownloadRequest downloadRequest){
        try {
            downloaderService.prepareDownload(downloadRequest);
            return ResponseEntity.status(HttpStatus.OK).body("{message: 'Ok'}");
        } catch (IOException e){
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró", e);
        } catch (ParseException e){
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al parsear", e);
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error sql", e);
        }
    }
}
