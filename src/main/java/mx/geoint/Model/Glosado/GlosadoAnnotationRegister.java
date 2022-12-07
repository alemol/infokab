package mx.geoint.Model.Glosado;

public class GlosadoAnnotationRegister {
    int projectID;
    String ANNOTATION_ID;
    String ANNOTATION_TIER_REF;
    String ANNOTATION_REF;
    String glosado;

    public GlosadoAnnotationRegister(){ }

    public void setANNOTATION_ID(String ANNOTATION_ID) { this.ANNOTATION_ID = ANNOTATION_ID; }

    public String getANNOTATION_ID() { return ANNOTATION_ID; }

    public void setProjectID(int projectID) { this.projectID = projectID; }

    public int getProjectID() { return projectID; }

    public void setANNOTATION_REF(String ANNOTATION_REF) { this.ANNOTATION_REF = ANNOTATION_REF; }

    public String getANNOTATION_REF() { return ANNOTATION_REF; }

    public void setANNOTATION_TIER_REF(String ANNOTATION_TIER_REF) { this.ANNOTATION_TIER_REF = ANNOTATION_TIER_REF; }

    public String getANNOTATION_TIER_REF() { return ANNOTATION_TIER_REF; }

    public void setGlosado(String glosado) { this.glosado = glosado; }

    public String getGlosado() { return glosado; }
}
