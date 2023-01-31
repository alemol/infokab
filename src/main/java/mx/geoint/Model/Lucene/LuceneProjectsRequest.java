package mx.geoint.Model.Lucene;

import java.util.ArrayList;

public class LuceneProjectsRequest {
    public ArrayList<LuceneProjectRequest> projectIndex;

    public LuceneProjectsRequest(){}

    public ArrayList<LuceneProjectRequest> getProjectIndex() { return projectIndex; }

    public void setProjectIndex(ArrayList<LuceneProjectRequest> projectIndex) { this.projectIndex = projectIndex; }
}