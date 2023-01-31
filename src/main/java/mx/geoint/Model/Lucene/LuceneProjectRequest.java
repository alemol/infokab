package mx.geoint.Model.Lucene;

public class LuceneProjectRequest {
    private String projectID;
    private Boolean maya;
    private Boolean spanish;
    private Boolean glosa;

    LuceneProjectRequest(){ }

    public void setProjectID(String projectID) { this.projectID = projectID; }

    public String getProjectID() { return projectID; }

    public Boolean getGlosa() { return glosa; }

    public void setSpanish(Boolean spanish) { this.spanish = spanish; }

    public Boolean getSpanish() { return spanish; }

    public void setMaya(Boolean maya) { this.maya = maya; }

    public Boolean getMaya() { return maya; }

    public void setGlosa(Boolean glosa) { this.glosa = glosa; }
}
