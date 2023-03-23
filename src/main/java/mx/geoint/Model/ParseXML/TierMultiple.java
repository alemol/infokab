package mx.geoint.Model.ParseXML;

public class TierMultiple {
    public String CVEGEO = "";
    public String TIME_SLOT_REF1 = "";
    public String TIME_VALUE1 = "";
    public String TIME_SLOT_REF2 = "";
    public String TIME_VALUE2 = "";

    public String REF_ANNOTATION_ID_TRANSCRIPCION_ORTOGRAFICA = "";
    public String REF_ANNOTATION_ID_TRADUCCION_LIBRE = "";
    public String REF_ANNOTATION_ID_GLOSA_INDEX = "";
    public String REF_ANNOTATION_ID_GLOSA_INDEX_WORDS = "";

    public String REF_ANNOTATION_REF_ID_TRANSCRIPCION_ORTOGRAFICA = "";
    public String REF_ANNOTATION_REF_ID_TRADUCCION_LIBRE = "";
    public String REF_ANNOTATION_REF_ID_GLOSA_INDEX = "";
    public String REF_ANNOTATION_REF_ID_INDEX_WORDS = "";

    public String ANNOTATION_VALUE_TRANSCRIPCION_ORTOGRAFICA = "";
    public String ANNOTATION_VALUE_TRADUCCION_LIBRE = "";
    public String ANNOTATION_VALUE_GLOSA_INDEX = "";
    public String ANNOTATION_VALUE_GLOSA_INDEX_WORDS = "";

    public double DIFF_TIME = 0.0;
    public String PROJECT_NAME;
    public String MEDIA_PATH;
    public String ORIGINAL_MEDIA_PATH;

    /**
     * Inserta el valor del nombre del proyecto
     * @param projectName String, Nombre del proyecto
     */
    public void setProjectName(String projectName){
        PROJECT_NAME = projectName;
    }

    /**
     * Inserta el valor del path multimedia
     * @param path String, path multimedia
     */
    public void setMediaPath(String path){
        MEDIA_PATH = path;
    }

    /**
     * Inserta el valor del path multimedia
     * @param path String, path original del multimedia
     */
    public void setOriginalMediaPath(String path){
        ORIGINAL_MEDIA_PATH = path;
    }

    public String getANNOTATION_VALUE_TRANSCRIPCION_ORTOGRAFICA() {
        return ANNOTATION_VALUE_TRANSCRIPCION_ORTOGRAFICA;
    }

    public String getANNOTATION_VALUE_TRADUCCION_LIBRE() {
        return ANNOTATION_VALUE_TRADUCCION_LIBRE;
    }

    public String getANNOTATION_VALUE_GLOSA_INDEX() {
        return ANNOTATION_VALUE_GLOSA_INDEX;
    }

    public String getANNOTATION_VALUE_GLOSA_INDEX_WORDS() {
        return ANNOTATION_VALUE_GLOSA_INDEX_WORDS;
    }

    public String getREF_ANNOTATION_REF_ID_TRANSCRIPCION_ORTOGRAFICA() {
        return REF_ANNOTATION_REF_ID_TRANSCRIPCION_ORTOGRAFICA;
    }

    public String getREF_ANNOTATION_REF_ID_TRADUCCION_LIBRE() {
        return REF_ANNOTATION_REF_ID_TRADUCCION_LIBRE;
    }

    public String getREF_ANNOTATION_REF_ID_GLOSA_INDEX() {
        return REF_ANNOTATION_REF_ID_GLOSA_INDEX;
    }

    public String getREF_ANNOTATION_REF_ID_INDEX_WORDS() {
        return REF_ANNOTATION_REF_ID_INDEX_WORDS;
    }

    public String getREF_ANNOTATION_ID_GLOSA_INDEX_WORDS() {
        return REF_ANNOTATION_ID_GLOSA_INDEX_WORDS;
    }


    public String getREF_ANNOTATION_ID_GLOSA_INDEX() {
        return REF_ANNOTATION_ID_GLOSA_INDEX;
    }

    public String getREF_ANNOTATION_ID_TRADUCCION_LIBRE() {
        return REF_ANNOTATION_ID_TRADUCCION_LIBRE;
    }

    public String getREF_ANNOTATION_ID_TRANSCRIPCION_ORTOGRAFICA() {
        return REF_ANNOTATION_ID_TRANSCRIPCION_ORTOGRAFICA;
    }

    public String getTIME_SLOT_REF2() {
        return TIME_SLOT_REF2;
    }

    public String getTIME_SLOT_REF1() {
        return TIME_SLOT_REF1;
    }

    public double getDIFF_TIME() { return DIFF_TIME; }

    public void setDIFF_TIME(double DIFF_TIME) { this.DIFF_TIME = DIFF_TIME; }

    public void setCVEGEO(String CVEGEO) { this.CVEGEO = CVEGEO; }

    public String getCVEGEO() { return CVEGEO; }
}
