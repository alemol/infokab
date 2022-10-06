package mx.geoint.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class GlosaAnnotationsRequest {
    private String project;

    @JsonProperty("ANNOTATION_ID")
    private String annotationID;

    @JsonProperty("ANNOTATION_REF")
    private String annotationREF;
    private ArrayList<GlosaStep> steps;

    public GlosaAnnotationsRequest(){ }
    public GlosaAnnotationsRequest(String project, String annotationID, ArrayList<GlosaStep> steps){
        this.project = project;
        this.annotationID = annotationID;
        this.steps = steps;
    }

    public void setProject(String project) {this.project = project; }
    public String getProject() { return project;}
    public void setAnnotationID(String annotationID) { this.annotationID = annotationID; }
    public String getAnnotationID() { return annotationID; }
    public void setSteps(ArrayList<GlosaStep> steps) { this.steps = steps; }
    public ArrayList<GlosaStep> getSteps() { return steps; }

    public void setAnnotationREF(String annotationREF) { this.annotationREF = annotationREF; }
    public String getAnnotationREF() { return annotationREF; }
}
