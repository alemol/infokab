package mx.geoint.Apis.Project;

import mx.geoint.Database.DBAnnotations;
import mx.geoint.Database.DBDictionary;
import mx.geoint.Database.DBProjects;
import mx.geoint.Database.DBReports;
import mx.geoint.Model.Project.ProjectPostgresRegister;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class ProjectService {
    public static DBProjects dbProjects;

    public ProjectService() {
        this.dbProjects = new DBProjects();
    }

    public ProjectPostgresRegister getProjectById(String project_id) throws SQLException {
        ProjectPostgresRegister projectPostgresRegister = this.dbProjects.getProjectById(project_id);
        return projectPostgresRegister;
    }

    public ArrayList<ProjectPostgresRegister> getProjects() throws SQLException {
        ArrayList<ProjectPostgresRegister> result = this.dbProjects.ListProjects();
        return result;
    }
}
