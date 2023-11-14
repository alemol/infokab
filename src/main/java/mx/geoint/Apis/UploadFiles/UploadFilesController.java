package mx.geoint.Apis.UploadFiles;

import com.google.gson.JsonObject;
import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Database.DBProjects;
import mx.geoint.Model.Project.ProjectPostgresLocationCoincidence;
import mx.geoint.Model.User.UserResponse;
import org.apache.commons.io.FileSystemUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@CrossOrigin(origins = {"http://www.taantsil.com","http://www.taantsil.com.mx/","http://www.infokaab.com/","http://www.infokaab.com.mx/","http://taantsil.com","http://taantsil.com.mx/","http://infokaab.com/","http://infokaab.com.mx/","http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009","http://10.2.102.182","http://10.2.102.189:3009"})
@RestController
@RequestMapping(path = "api/upload")
public class UploadFilesController {
    private final UploadFilesService uploadFilesService;
    private Logger logger;

    public UploadFilesController(UploadFilesService uploadFilesService) {
        this.uploadFilesService = uploadFilesService;
        this.logger = new Logger();
    }

    /**
     * @param eaf         MultipartFile, Archivo de anotaciones
     * @param multimedia  MultipartFile, Archivo de multimedia audio o video
     * @param uuid        String, Identificador de usuario
     * @param projectName String, Nombre del proyecto
     * @return
     * @throws IOException
     */
    @RequestMapping(path="/new", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity created(@RequestParam MultipartFile eaf, @RequestParam MultipartFile multimedia, @RequestParam String uuid, @RequestParam String projectName, @RequestParam(required = false) MultipartFile autorizacion, @RequestParam String date, @RequestParam String hablantes, @RequestParam String ubicacion, @RequestParam String radio, @RequestParam String circleBounds, @RequestParam(required = false) MultipartFile[] images,@RequestParam String localidad_nombre, @RequestParam String localidad_cvegeo, @RequestParam String mimeType) throws IOException {

        Date startDate = new Date();
        if (eaf.isEmpty() || multimedia.isEmpty()) {
            return createdResponseEntity(HttpStatus.BAD_REQUEST, "Error se requiere 1 archivo .eaf y 1 archivo multimedia", false);
        }

        if(mimeType.isEmpty()){
            return createdResponseEntity(HttpStatus.BAD_REQUEST, "Error en el tipo de multimedia", false);
        }

        if (uuid.isEmpty()) {
            return createdResponseEntity(HttpStatus.BAD_REQUEST, "Error se requiere el uuid del usuario", false);
        }

        if (projectName.isEmpty()) {
            return createdResponseEntity(HttpStatus.BAD_REQUEST, "Error se requiere nombre del proyecto", false);
        }

        if(images != null){
            for(MultipartFile file : images) {
                if(!file.getContentType().equals("video/mp4") && !file.getContentType().equals("image/jpg") && !file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png") ){
                    return createdResponseEntity(HttpStatus.BAD_REQUEST, "Error en formato desconocido de multimedia", false);
                }
            }
        }

        try{
            long uploadTime = (new Date()).getTime();
        Number codeStatus = uploadFilesService.uploadFile(eaf, multimedia, autorizacion, images, uuid, projectName + "_" + uploadTime, date, hablantes, ubicacion, radio, circleBounds, localidad_nombre, localidad_cvegeo, mimeType);

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
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SQLException", e);
        } catch (IOException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "IOException", e);
        }

    }

    @RequestMapping(path="/update/eaf", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity updateEaf(@RequestParam MultipartFile eaf, @RequestParam String projectName, @RequestParam String uuid, @RequestParam int id) {
        if(eaf.isEmpty()) {
            return createdResponseEntity(HttpStatus.BAD_REQUEST, "Error se requiere 1 archivo .eaf", false);
        }

        try{
            Number codeStatus = uploadFilesService.updateEaf(eaf, projectName, uuid, id);
            if(codeStatus.equals(0)){
                return createdResponseEntity(HttpStatus.OK, "success", true);
            }else{
                return createdResponseEntity(HttpStatus.CONFLICT, codeStatus.toString(), false);
            }
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SQLException", e);
        } catch (IOException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "IOException", e);
        }
    }

    @RequestMapping(path="/update/multimedia", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity updateMultimedia(@RequestParam MultipartFile multimedia, @RequestParam String projectName, @RequestParam String uuid, @RequestParam int id) {
        if(multimedia.isEmpty()) {
            return createdResponseEntity(HttpStatus.BAD_REQUEST, "Error se requiere 1 archivo multimedia", false);
        }

        try{
            Number codeStatus = uploadFilesService.updateMultimedia(multimedia, projectName, uuid, id);
            if(codeStatus.equals(0)){
                return createdResponseEntity(HttpStatus.OK, "success", true);
            }else{
                return createdResponseEntity(HttpStatus.CONFLICT, codeStatus.toString(), false);
            }
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SQLException", e);
        } catch (IOException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "IOException", e);
        }
    }

    @RequestMapping(path="/update/images", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity updateImages(@RequestParam MultipartFile[] images, @RequestParam String projectName, @RequestParam String uuid, @RequestParam int id) {
        if(images.length == 0) {
            return createdResponseEntity(HttpStatus.BAD_REQUEST, "Error se requiere al menos una imagen", false);
        }

        for(MultipartFile file : images) {
            if(!file.getContentType().equals("video/mp4") && !file.getContentType().equals("image/jpg") && !file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png") ){
                return createdResponseEntity(HttpStatus.BAD_REQUEST, "Error en formato desconocido de multimedia", false);
            }
        }

        try{
            Number codeStatus = uploadFilesService.updateImages(images, projectName, uuid, id);
            if(codeStatus.equals(0)){
                return createdResponseEntity(HttpStatus.OK, "success", true);
            }else{
                return createdResponseEntity(HttpStatus.CONFLICT, codeStatus.toString(), false);
            }
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SQLException", e);
        } catch (IOException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "IOException", e);
        }
    }

    /**
     * @param code    HttpStatus, codigo Http
     * @param message String, Mensaje de respuesta
     * @param status  boolean, respuesta del success
     * @return
     */
    public ResponseEntity createdResponseEntity(HttpStatus code, String message, boolean status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");
        JsonObject answerJsonObject = new JsonObject();

        answerJsonObject.addProperty("success", status);

        if (status) {
            answerJsonObject.addProperty("uuid", message);
        } else {
            answerJsonObject.addProperty("error", message);
        }

        return new ResponseEntity<>(answerJsonObject.toString(), headers, code);
    }

    @RequestMapping(path="/validate/link", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<URL> validateLink(@RequestBody Map<String, String> body) {
        String link = body.get("link");
        System.out.println(link);
        try{
            URL url = uploadFilesService.validateLink(link);
            return ResponseEntity.status(HttpStatus.OK).body(url);
        } catch (MalformedURLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SQLException", e);
        } catch (IOException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SQLException", e);
        }
    }

    @RequestMapping(path="/validate/location", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<ProjectPostgresLocationCoincidence>> validateLocation(@RequestParam String projectName, @RequestParam String ubicacion) throws IOException, SQLException {

        try {
            ArrayList<ProjectPostgresLocationCoincidence> response = DBProjects.checkLocation(projectName, ubicacion);
            if(response.size()==1){
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }else{
                return createdResponseEntity(HttpStatus.CONFLICT, "No coinciden los CVEGEO", false);
            }

        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SQLException", e);
        }
    }
}
