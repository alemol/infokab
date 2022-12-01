package mx.geoint.Model.Glosado;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GlosaUpdateAnnotationRequest {
    private String filePath;

    @JsonProperty("ANNOTATION_ID")
    private String annotationID;

    @JsonProperty("ANNOTATION_REF")
    private String annotationREF;

    @JsonProperty("ANNOTATION_ORIGINAL")
    private String annotationOriginal;

    @JsonProperty("ANNOTATION_VALUE")
    private String annotationValue;

    private Integer projectID;
    private String title;
    private String report;
    private String type;
    public GlosaUpdateAnnotationRequest(){ }

    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFilePath() { return filePath; }

    public void setAnnotationREF(String annotationREF) { this.annotationREF = annotationREF; }

    public String getAnnotationID() { return annotationID; }

    public void setAnnotationID(String annotationID) { this.annotationID = annotationID; }

    public String getAnnotationREF() { return annotationREF; }

    public void setAnnotationValue(String annotationValue) { this.annotationValue = annotationValue; }

    public String getAnnotationValue() { return annotationValue; }

    public void setAnnotationOriginal(String annotationOriginal) { this.annotationOriginal = annotationOriginal; }

    public String getAnnotationOriginal() { return annotationOriginal; }

    public void setReport(String report) { this.report = report; }

    public String getReport() { return report; }

    public void setProjectID(Integer projectID) { this.projectID = projectID; }

    public Integer getProjectID() { return projectID; }

    public void setTitle(String title) { this.title = title; }

    public String getTitle() { return title; }

    public void setType(String type) { this.type = type; }

    public String getType() { return type; }
}
