package mx.geoint.Apis.Logger;

import mx.geoint.Controllers.Logger.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = {"http://infokaab.com/","http://infokaab.com.mx/","http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009","http://10.2.102.182"})
@RestController
@RequestMapping(path = "api/logger")
public class LoggerController {
    private final LoggerService loggerService;
    private Logger logger;

    public LoggerController(LoggerService loggerService) {
        this.loggerService = loggerService;
        this.logger = new Logger();
    }

    @RequestMapping(path="/list", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<String>> listLogger(@RequestBody Map<String, String> body) {
        String uuid = body.get("uuid");
        ArrayList<String> response = loggerService.getLoggerFiles();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(path="/file", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, String>> readLoggerFile(@RequestBody Map<String, String> body){
        try{
            String fileName = body.get("fileName");
            String text = loggerService.readLoggerFile(fileName);
            Map<String, String> map = new HashMap<>();
            map.put("text", text);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }catch (IOException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se encontro", e);
        }
    }
}
