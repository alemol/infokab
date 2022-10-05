package mx.geoint.UploadFiles;

import com.google.gson.JsonObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@CrossOrigin(origins = {"http://infokaab.com/","http://infokaab.com.mx/","http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009","http://10.2.102.182"})
@RestController
@RequestMapping(path = "api/upload")
public class UploadFilesController {
    private final UploadFilesService uploadFilesService;

    public UploadFilesController(UploadFilesService uploadFilesService) {
        this.uploadFilesService = uploadFilesService;
    }

    /**
     *
     * @param eaf MultipartFile, Archivo de anotaciones
     * @param multimedia MultipartFile, Archivo de multimedia audio o video
     * @param uuid String, Identificador de usuario
     * @param projectName String, Nombre del proyecto
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity created(@RequestParam MultipartFile eaf, @RequestParam MultipartFile multimedia, @RequestParam String uuid, @RequestParam String projectName) throws IOException {
        Date startDate = new Date();

        if (eaf.isEmpty() || multimedia.isEmpty()) {
            return createdResponseEntity(HttpStatus.BAD_REQUEST, "Error se requiere 1 archivo .eaf y 1 archivo multimedia", false);
        }

        if (uuid.isEmpty()) {
            return createdResponseEntity(HttpStatus.BAD_REQUEST, "Error se requiere el uuid del usuario", false);
        }

        if (projectName.isEmpty()) {
            return createdResponseEntity(HttpStatus.BAD_REQUEST, "Error se requiere nombre del proyecto", false);
        }

        long uploadTime = (new Date()).getTime();
        Number codeStatus = uploadFilesService.uploadFile(eaf, multimedia, uuid, projectName+"_"+uploadTime);

        Date endDate = new Date();
        long difference_In_Time = endDate.getTime() - startDate.getTime();
        long difference_In_Seconds = (difference_In_Time / (1000)) % 60;
        long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
        System.out.println("TIMER FINISHED API: " + difference_In_Seconds + "s " + difference_In_Minutes + "m");

        if(codeStatus.equals(0)){
            return createdResponseEntity(HttpStatus.OK, uuid, true);
        }else{
            return createdResponseEntity(HttpStatus.CONFLICT, codeStatus.toString(), false);
        }
    }

    /**
     *
     * @param code HttpStatus, codigo Http
     * @param message String, Mensaje de respuesta
     * @param status boolean, respuesta del success
     * @return
     */
    public ResponseEntity createdResponseEntity(HttpStatus code, String message, boolean status){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");
        JsonObject answerJsonObject = new JsonObject();

        answerJsonObject.addProperty("success", status);

        if(status){
            answerJsonObject.addProperty("uuid", message);
        }else{
            answerJsonObject.addProperty("error", message);
        }

        return new ResponseEntity<>(answerJsonObject.toString(), headers, code);
    }
}
