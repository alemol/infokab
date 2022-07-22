package mx.geoint.UploadFiles;

import com.google.gson.JsonObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping(path = "api/upload")
public class UploadFilesController {
    private final UploadFilesService uploadFilesService;

    public UploadFilesController(UploadFilesService uploadFilesService) {
        this.uploadFilesService = uploadFilesService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity created(@RequestParam MultipartFile eaf, @RequestParam MultipartFile multimedia, @RequestParam String uuid) throws IOException {
        Date startDate = new Date();

        if (eaf.isEmpty() || multimedia.isEmpty()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json; charset=UTF-8");

            JsonObject answerJsonObject = new JsonObject();
            answerJsonObject.addProperty("success", false);
            answerJsonObject.addProperty("error", "Error se requiere 1 archivo .eaf y 1 archivo multimedia");
            return new ResponseEntity<>(answerJsonObject.toString(), headers, HttpStatus.BAD_REQUEST);
        }

        if(uuid.isEmpty()){
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json; charset=UTF-8");

            JsonObject answerJsonObject = new JsonObject();
            answerJsonObject.addProperty("success", false);
            answerJsonObject.addProperty("error", "Error se requiere el uuid del usuario");
            return new ResponseEntity<>(answerJsonObject.toString(), headers, HttpStatus.BAD_REQUEST);
        }

        uploadFilesService.uploadFile(eaf, multimedia, uuid);

        Date endDate = new Date();
        long difference_In_Time = endDate.getTime() - startDate.getTime();
        long difference_In_Seconds = (difference_In_Time / (1000)) % 60;
        long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
        System.out.println("TIMER FINISHED API: "+ difference_In_Seconds +"s " + difference_In_Minutes+"m");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");
        JsonObject answerJsonObject = new JsonObject();
        answerJsonObject.addProperty("success", true);
        answerJsonObject.addProperty("uuid", uuid);
        return new ResponseEntity<>(answerJsonObject.toString(), headers, HttpStatus.OK);
    }
}
