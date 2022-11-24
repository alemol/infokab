package mx.geoint.Model.Glosado;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GlosaReportWordRequest {
    private String filePath;

    @JsonProperty("ANNOTATION_ID")
    private String annotationID;

    @JsonProperty("ANNOTATION_REF")
    private String annotationREF;

    private String word;
    private String report;

    public GlosaReportWordRequest(){ }

    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFilePath() { return filePath; }

    public void setAnnotationREF(String annotationREF) { this.annotationREF = annotationREF; }

    public String getAnnotationID() { return annotationID; }

    public void setAnnotationID(String annotationID) { this.annotationID = annotationID; }

    public String getAnnotationREF() { return annotationREF; }

    public void setWord(String word) { this.word = word; }

    public String getWord() { return word; }


    public void setReport(String report) { this.report = report; }

    public String getReport() { return report; }
}
