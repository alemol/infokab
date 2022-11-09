package mx.geoint.Glosa.Analysis;

import mx.geoint.Glosa.Dictionary.DictionaryPaginate;
import mx.geoint.Glosa.Dictionary.DictionaryRequest;
import mx.geoint.Model.*;
import mx.geoint.ParseXML.Tier;
import mx.geoint.Response.ReportsResponse;
import mx.geoint.database.DBProjects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping(path = "api/glosa")
public class GlosaController {
    private final GlosaService glosaService;

    public GlosaController(GlosaService glosaService) {
        this.glosaService = glosaService;
    }

    /**
     * Api para ejectuar el script de python
     * @return ArrayList<Glosa> arreglo del modelo glosa
     * @throws IOException
     */
    @RequestMapping(path="/script", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<Glosa>> created() throws IOException {
        Date startDate = new Date();
        ArrayList<Glosa> response = glosaService.process();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Api para análisis de una oracion o anotación
     * @param body text a procesas y indentificador del usuario
     * @return ArrayList<Glosa> arreglo del modelo glosa
     * @throws IOException
     * @throws SQLException
     */
    @RequestMapping(path="/analysis", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<Glosa>> analysis(@RequestBody Map<String, String> body) throws IOException, SQLException {
        //System.out.println("data:"+body.get("uuid"));
        String text = body.get("text");
        ArrayList<Glosa> response = glosaService.textProcess(text);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Api para análisis de una lista de oraciones o anotaciones
     * @param glosaRequest
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @RequestMapping(path="/analysis/list", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<Glosa>> arrayAnalisys(@RequestBody GlosaRequest glosaRequest) throws IOException, SQLException {
        ArrayList<Glosa> response = glosaService.ArrayProcess(glosaRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(path="/project", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProjectRegistration> getProjectById(@RequestBody Map<String, String> body) throws IOException, SQLException {
        String project_id = body.get("project");

        DBProjects dbProjects = new DBProjects();
        ProjectRegistration result = dbProjects.getProjectById(project_id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * Api para obtener una lista de proyecto predeterminados
     * @return ArraList<String> Lista de los nombre de los archivos
     * @throws IOException
     */
    @RequestMapping(path="/projects", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<?>> getFiles() throws IOException, SQLException {
        //ArrayList<String> arrayList = new ArrayList<>();
        //arrayList.add("04_02_01062022_11_SCY_C_2_2");
        //arrayList.add("04_02_01072022_11_SCY_C_2_2");
        //arrayList.add("04_02_01082022_11_SCY_C_2_2");
        //arrayList.add("04_02_01092022_11_SCY_C_2_2");

        DBProjects dbProjects = new DBProjects();
        ArrayList<ProjectRegistration> result = dbProjects.ListProjects();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * Api para obtener las oraciones o anotaciones de un proyecto predeterminado
     * @param body
     * @return ArrayList<Tier> Lista de las oraciones o anotaciones obtenidas del archivos eaf de un proyecto predeterminado
     * @throws IOException
     */
    @RequestMapping(path="/annotations", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<Tier>> getAnnotations(@RequestBody Map<String, String> body) throws IOException {
        String filePath = body.get("filePath");
        String project_id = body.get("project");
        ArrayList<Tier> arrayList = glosaService.getAnnotations(filePath, project_id);
        return ResponseEntity.status(HttpStatus.OK).body(arrayList);
    }

    /**
     *
     * @param dictionaryPaginate
     * @return
     */
    @RequestMapping(path="/reports/list", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ReportsResponse> getReports(@RequestBody DictionaryPaginate dictionaryPaginate) {
        try{
            ReportsResponse reportsResponse = glosaService.getRegisters(dictionaryPaginate);
            return ResponseEntity.status(HttpStatus.OK).body(reportsResponse);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(path="/report", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public boolean createRegisterBases(@RequestBody ReportRequest ReportRequest) throws SQLException {
        return glosaService.insertRegister(ReportRequest);
    }

    /**
     * Api para el guardado del análisis de una oración o anotación al archivo eaf correspondiente de un proyecto
     * @param glosaAnnotationsRequest
     * @return
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws SAXException
     */
    @RequestMapping(path="/save/annotation", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> saveAnnotations(@RequestBody GlosaAnnotationsRequest glosaAnnotationsRequest) throws IOException, ParserConfigurationException, TransformerException, SAXException {
        Boolean answer = glosaService.saveAnnotation(glosaAnnotationsRequest);
        return ResponseEntity.status(HttpStatus.OK).body(answer);
    }
}
