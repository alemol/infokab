package mx.geoint.ParseXML;

public class Tier {
    public String ANNOTATION_ID = "";
    public String TIME_SLOT_REF1 = "";
    public String TIME_VALUE1 = "";
    public String TIME_SLOT_REF2 = "";
    public String TIME_VALUE2 = "";
    public String ANNOTATION_VALUE = "";

    public double DIFF_TIME = 0.0;

    /*
     * Inicializa la anotacion con sus respesctivos tiempos
     * @param
     *  annotation_id   indentificador de la anotación
     *  time_slot_ref1  indentificador de inicio del la anotación
     *  time_value1     tiempo de inicio de la anotación en milisengundos
     *  time_slot_ref2  indentificador de fin de la anotación
     *  time_value1     tiempo de fin de la anotacion en milisengundos
     **/
    public Tier(String annotation_id, String time_slot_ref1, String time_value1, String time_slot_ref2, String time_value2) {
        this.ANNOTATION_ID = annotation_id;
        this.TIME_SLOT_REF1 = time_slot_ref1;
        this.TIME_SLOT_REF2 = time_slot_ref2;
        this.TIME_VALUE1 = time_value1;
        this.TIME_VALUE2 = time_value2;

        try{
            this.DIFF_TIME = Integer.parseInt(time_value2) - Integer.parseInt(time_value1);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Inserta la el valor de la anotación
     * @param
     *  annotation_value texto de la anotación
     *  time_slot_ref1  indentificador de inicio del la anotación
     **/
    public void setAnnotationValue(String annotation_value){
        ANNOTATION_VALUE = annotation_value;
    }
}
