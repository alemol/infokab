package mx.geoint.Model.Project;

import java.util.ArrayList;

public class ProjectPostgresGeometry {
    ArrayList<ProjectPostgresLocations> projectPostgresLocations;
    ArrayList<String> bbox;

    public ProjectPostgresGeometry(){}

    public void setBbox(ArrayList<String> bbox) { this.bbox = bbox; }

    public ArrayList<String> getBbox() { return bbox; }

    public void setProjectPostgresLocations(ArrayList<ProjectPostgresLocations> projectPostgresLocations) { this.projectPostgresLocations = projectPostgresLocations; }

    public ArrayList<ProjectPostgresLocations> getProjectPostgresLocations() { return projectPostgresLocations; }
}
