package mx.geoint.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class GlosaAnnotationsRequest {
    private String filePath;
    private int projectID;

    @JsonProperty("ANNOTATION_ID")
    private String annotationID;

    @JsonProperty("ANNOTATION_REF")
    private String annotationREF;
    private ArrayList<GlosaStep> steps;

    public GlosaAnnotationsRequest(){ }
    public GlosaAnnotationsRequest(String filePath, String annotationID, ArrayList<GlosaStep> steps){
        this.filePath = filePath;
        this.annotationID = annotationID;
        this.steps = steps;
    }

    public void setFilePath(String filePath) {this.filePath = filePath; }
    public String getFilePath() { return filePath;}
    public void setAnnotationID(String annotationID) { this.annotationID = annotationID; }
    public String getAnnotationID() { return annotationID; }
    public void setSteps(ArrayList<GlosaStep> steps) { this.steps = steps; }
    public ArrayList<GlosaStep> getSteps() { return steps; }

    public void setAnnotationREF(String annotationREF) { this.annotationREF = annotationREF; }
    public String getAnnotationREF() { return annotationREF; }

    public void setProjectID(int projectID) { this.projectID = projectID; }

    public int getProjectID() { return projectID; }
}
