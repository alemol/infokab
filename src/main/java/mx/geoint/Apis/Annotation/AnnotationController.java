package mx.geoint.Apis.Annotation;

import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Model.Annotation.AnnotationRequest;
import mx.geoint.Model.ParseXML.Tier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@CrossOrigin(origins = {"http://infokaab.com/","http://infokaab.com.mx/","http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009","http://10.2.102.182"})
@RestController
@RequestMapping(path="api/annotation")
public class AnnotationController {
    private final AnnotationService annotationService;
    private Logger logger;

    public AnnotationController(AnnotationService annotationService) {
        this.annotationService = annotationService;
        this.logger = new Logger();
    }

    @RequestMapping(path="/update", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> editAnnotation(@RequestBody AnnotationRequest annotationRequest) {
        try{
            Boolean answer = annotationService.editAnnotation(annotationRequest);
            return ResponseEntity.status(HttpStatus.OK).body(answer);
        } catch (ParserConfigurationException e){
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ParserConfigurationException", e);
        } catch (SAXException e){
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "SAXException", e);
        } catch (IOException e){
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "IOException", e);
        } catch (TransformerException e){
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TransformerException", e);
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SQLException", e);
        }
    }

    /**
     * Api para obtener las oraciones o anotaciones de un proyecto predeterminado
     * @param body
     * @return ArrayList<Tier> Lista de las oraciones o anotaciones obtenidas del archivos eaf de un proyecto predeterminado
     */
    @RequestMapping(path="/registers", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<Tier>> getAnnotations(@RequestBody Map<String, String> body) {
        try{
            String filePath = body.get("filePath");
            String project_id = body.get("project");
            ArrayList<Tier> arrayList = annotationService.getAnnotations(filePath, project_id);
            return ResponseEntity.status(HttpStatus.OK).body(arrayList);
        } catch (ParserConfigurationException e){
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ParserConfigurationException", e);
        } catch (SAXException e){
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "SAXException", e);
        } catch (IOException e){
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "IOException", e);
        }
    }
}
