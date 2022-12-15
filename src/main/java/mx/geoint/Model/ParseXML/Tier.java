package mx.geoint.Model.ParseXML;

import mx.geoint.Controllers.Logger.Logger;

public class Tier {
    private Logger logger = new Logger();
    public String ANNOTATION_ID = "";
    public String ANNOTATION_REF = "";
    public String TIME_SLOT_REF1 = "";
    public String TIME_VALUE1 = "";
    public String TIME_SLOT_REF2 = "";
    public String TIME_VALUE2 = "";
    public String ANNOTATION_VALUE = "";

    public double DIFF_TIME = 0.0;
    public String PROJECT_NAME;
    public String MEDIA_PATH;
    public String ORIGINAL_MEDIA_PATH;

    /**
     * Inicializa la anotación con sus respectivos tiempos
     * @param annotation_id String, identificador de la anotación
     * @param time_slot_ref1 String, identificador de inicio del la anotación
     * @param time_value1 String, tiempo de inicio de la anotación en milisengundos
     * @param time_slot_ref2 String, identificador de fin de la anotación
     * @param time_value2 String, tiempo de fin de la anotación en milisengundos
     */
    public Tier(String annotation_id, String time_slot_ref1, String time_value1, String time_slot_ref2, String time_value2) {
        this.ANNOTATION_ID = annotation_id;
        this.TIME_SLOT_REF1 = time_slot_ref1;
        this.TIME_SLOT_REF2 = time_slot_ref2;
        this.TIME_VALUE1 = time_value1;
        this.TIME_VALUE2 = time_value2;

        try{
            this.DIFF_TIME = Integer.parseInt(time_value2) - Integer.parseInt(time_value1);
        } catch (NumberFormatException e) {
            logger.appendToFile(e);
        }
    }

    public Tier(String annotation_id, String time_slot_ref1, String time_value1, String time_slot_ref2, String time_value2, String annotation_ref) {
        this.ANNOTATION_ID = annotation_id;
        this.TIME_SLOT_REF1 = time_slot_ref1;
        this.TIME_SLOT_REF2 = time_slot_ref2;
        this.TIME_VALUE1 = time_value1;
        this.TIME_VALUE2 = time_value2;
        this.ANNOTATION_REF = annotation_ref;

        try{
            this.DIFF_TIME = Integer.parseInt(time_value2) - Integer.parseInt(time_value1);
        } catch (NumberFormatException e) {
            logger.appendToFile(e);
        }
    }

    /**
     * Inserta el valor de la anotación
     * @param annotation_value String, texto de la anotación
     */
    public void setAnnotationValue(String annotation_value){
        ANNOTATION_VALUE = annotation_value;
    }

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
}
