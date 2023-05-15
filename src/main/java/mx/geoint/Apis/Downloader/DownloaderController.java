package mx.geoint.Apis.Downloader;

import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Model.Download.DownloadRequest;
import mx.geoint.pathSystem;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@CrossOrigin(origins = {"http://taantsil.com","http://taantsil.com.mx/","http://infokaab.com/","http://infokaab.com.mx/","http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009","http://10.2.102.182"})
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
            return ResponseEntity.status(HttpStatus.OK).body("ok");
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

    @RequestMapping(path = "/file", method = RequestMethod.POST, produces = "application/zip")
    public @ResponseBody byte[] getFile(@RequestBody String fileName) throws IOException{
        System.out.println("File/Descargas/" + fileName + ".zip");
        InputStream in = getClass().getResourceAsStream("File/Descargas/" + fileName + ".zip");
        return IOUtils.toByteArray(in);
    }

    @RequestMapping(path="/list", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<String>> listLogger(@RequestBody Map<String, String> body) {
        String uuid = body.get("uuid");
        ArrayList<String> response = downloaderService.getLoggerFiles();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
