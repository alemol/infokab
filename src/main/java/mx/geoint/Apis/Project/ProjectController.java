package mx.geoint.Apis.Project;

import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Database.DBProjects;
import mx.geoint.Model.Project.ProjectPostgresRegister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

//@CrossOrigin(origins = {"http://infokaab.com/","http://infokaab.com.mx/","http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009","http://10.2.102.182"})
@RestController
@RequestMapping(path = "api/project")
public class ProjectController {
    private final ProjectService projectService;
    private Logger logger;

    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
        this.logger = new Logger();
    }

    @RequestMapping(path="/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProjectPostgresRegister> getProjectById(@RequestBody Map<String, String> body)  {
        try{
            String project_id = body.get("project");
            ProjectPostgresRegister result = projectService.getProjectById(project_id);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "SQLException", e);
        }
    }

    @RequestMapping(path="/registers", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArrayList<?>> getProjects() {
        try {
            ArrayList<ProjectPostgresRegister> result = projectService.getProjects();
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }catch (SQLException e){
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "SQLException", e);
        }
    }

    @RequestMapping(path="/delete", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> deleteProject(@RequestBody Map<String, String> body) {
        try {
            String projectID = body.get("projectID");
            String projectName = body.get("projectName");
            boolean result  = projectService.deleteProject(projectID, projectName);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }catch (SQLException e){
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "SQLException", e);
        }
    }
}
