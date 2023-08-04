package mx.geoint.Apis.DataManagement;

import mx.geoint.Controllers.Logger.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import static mx.geoint.Controllers.DataManagement.DataManagement.getDiskSize;

@CrossOrigin(origins = {"http://taantsil.com","http://taantsil.com.mx/","http://infokaab.com/", "http://infokaab.com.mx/", "http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009", "http://10.2.102.182"})
@RestController
@RequestMapping(path = "api/storage")
public class DataManagement {
    private Logger logger;
    private final DataManagementService dataManagementService;

    public DataManagement(DataManagementService dataManagementService){
        this.dataManagementService = dataManagementService;
        this.logger = new Logger();
    }

    @RequestMapping(path = "/getStorage", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getStorage() throws SQLException {
        Map<String, Object> mapSizes = getDiskSize("/");
        return ResponseEntity.status(HttpStatus.OK).body(mapSizes);
    }

    @RequestMapping(path = "/setStorage", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity setStorage(@RequestBody Map<String, String> body) throws SQLException, IOException, InterruptedException {
        String project_id = body.get("project");
        System.out.println("project_id : " + project_id);
        boolean rs = dataManagementService.setStorage(project_id);
        return ResponseEntity.status(HttpStatus.OK).body(rs);
    }
}


