package mx.geoint.Apis.Glosa;

import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Model.Annotation.AnnotationsRequest;
import mx.geoint.Model.Annotation.AnnotationRequest;
import mx.geoint.Model.Annotation.AnnotationRegister;
import mx.geoint.Model.General.GeneralPaginateResponse;
import mx.geoint.Model.Glosado.*;
import mx.geoint.Model.ParseXML.Tier;
import mx.geoint.Model.Project.ProjectPostgresRegister;
import mx.geoint.Model.Report.ReportRequest;
import mx.geoint.Model.Report.ReportsResponse;
import mx.geoint.Database.DBProjects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

//@CrossOrigin(origins = {"http://infokaab.com/","http://infokaab.com.mx/","http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009","http://10.2.102.182"})
@RestController
@RequestMapping(path = "api/glosa")
public class GlosaController {
    private final GlosaService glosaService;
    private Logger logger;
    public GlosaController(GlosaService glosaService) {
        this.glosaService = glosaService;
        this.logger = new Logger();
    }

    /**
     * Api para análisis de una oracion o anotación
     * @param body text a procesas y indentificador del usuario
     * @return ArrayList<Glosa> arreglo del modelo glosa
     */
    @RequestMapping(path="/analysis", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<GlosaResponse>> analysis(@RequestBody Map<String, String> body) {
        try{
            String text = body.get("text");
            ArrayList<GlosaResponse> response = glosaService.textProcess(text);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch(SQLException e){
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "SQLException", e);
        }
    }

    /**
     * Api para análisis de una lista de oraciones o anotaciones
     * @param glosaRequest
     * @return
     */
    @RequestMapping(path="/analysis/list", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<GlosaResponse>> arrayAnalisys(@RequestBody GlosaRequest glosaRequest) {
        try {
            ArrayList<GlosaResponse> response = glosaService.ArrayProcess(glosaRequest);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch(SQLException e){
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "SQLException", e);
        }
    }

    /**
     * Api para el guardado del análisis de una oración o anotación al archivo eaf correspondiente de un proyecto
     * @param annotationsRequest
     * @return
     */
    @RequestMapping(path="/save/annotation", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> saveAnnotations(@RequestBody AnnotationsRequest annotationsRequest) {
        try{
            Boolean answer = glosaService.saveAnnotation(annotationsRequest);
            return ResponseEntity.status(HttpStatus.OK).body(answer);
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SQLException", e);
        } catch (ParserConfigurationException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ParserConfigurationException", e);
        } catch (IOException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "IOException", e);
        } catch (TransformerException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TransformerException", e);
        } catch (SAXException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SAXException", e);
        }
    }

    @RequestMapping(path="/saved/annotation/v2", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> saveAnnotationsV2(@RequestBody AnnotationsRequest annotationsRequest) {
        try{
            Boolean answer = glosaService.savedAnnotationV2(annotationsRequest);
            return ResponseEntity.status(HttpStatus.OK).body(answer);
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SQLException", e);
        } catch (ParserConfigurationException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ParserConfigurationException", e);
        } catch (IOException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "IOException", e);
        } catch (TransformerException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TransformerException", e);
        } catch (SAXException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SAXException", e);
        }
    }

    @RequestMapping(path="/saved/annotation/v3", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> saveAnnotationsV3(@RequestBody AnnotationsRequest annotationsRequest) {
        try{
            Boolean answer = glosaService.savedAnnotationV3(annotationsRequest);
            return ResponseEntity.status(HttpStatus.OK).body(answer);
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SQLException", e);
        } catch (ParserConfigurationException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ParserConfigurationException", e);
        } catch (IOException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "IOException", e);
        } catch (TransformerException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TransformerException", e);
        } catch (SAXException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SAXException", e);
        }
    }

    @RequestMapping(path="/saved/annotation/eaf", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> saveAnnotationsToEaf(@RequestBody Map<String, String> body) {
        String filePath = body.get("filePath");
        String projectID = body.get("projectID");

        try{
            Boolean answer = glosaService.saveAnnotationsToEaf(Integer.parseInt(projectID), filePath);
            return ResponseEntity.status(HttpStatus.OK).body(answer);
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SQLException", e);
        } catch (ParserConfigurationException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ParserConfigurationException", e);
        } catch (IOException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "IOException", e);
        } catch (TransformerException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TransformerException", e);
        } catch (SAXException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SAXException", e);
        }
    }

    @RequestMapping(path="saved/annotation/list", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<AnnotationRegister>> savedAnnotationsList(@RequestBody Map<String, String> body) {
        String uuid = body.get("uuid");
        String project_id = body.get("project");

        try{
            ArrayList<AnnotationRegister> answer = glosaService.savedAnnotationList(Integer.parseInt(project_id));
            return ResponseEntity.status(HttpStatus.OK).body(answer);
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SQLException", e);
        }
    }

    @RequestMapping(path="saved/annotation/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AnnotationRegister> getAnnotationRecord(@RequestBody Map<String, String> body) {
        String uuid = body.get("uuid");
        String project_id = body.get("project");
        String annotation_ref = body.get("annotation_ref");

        try{
            AnnotationRegister answer = glosaService.getAnnotationRecord(Integer.parseInt(project_id), annotation_ref);
            return ResponseEntity.status(HttpStatus.OK).body(answer);
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SQLException", e);
        }
    }
}
