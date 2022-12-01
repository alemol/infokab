package mx.geoint.Model.Glosado;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GlosaUpdateAnnotationRequest {
    private String filePath;

    @JsonProperty("ANNOTATION_ID")
    private String annotationID;

    @JsonProperty("ANNOTATION_REF")
    private String annotationREF;

    @JsonProperty("ANNOTATION_VALUE")
    private String annotationValue;

    public GlosaUpdateAnnotationRequest(){ }

    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFilePath() { return filePath; }

    public void setAnnotationREF(String annotationREF) { this.annotationREF = annotationREF; }

    public String getAnnotationID() { return annotationID; }

    public void setAnnotationID(String annotationID) { this.annotationID = annotationID; }

    public String getAnnotationREF() { return annotationREF; }

    public void setAnnotationValue(String annotationValue) { this.annotationValue = annotationValue; }

    public String getAnnotationValue() { return annotationValue; }
}
