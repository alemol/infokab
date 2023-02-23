package mx.geoint.Controllers.ParseXML;

import com.google.gson.JsonObject;
import mx.geoint.pathSystem;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.Normalizer;

public class ParseHandlerMultiple extends DefaultHandler {

    //VARIABLES PARA LOS STAGS IMPORTANTE DEL ARCHIVO .EAF
    private static final String ANNOTATION_DOCUMENT = "ANNOTATION_DOCUMENT";
    private static final String HEADER = "HEADER";
    private static final String MEDIA_DESCRIPTOR = "MEDIA_DESCRIPTOR";
    private static final String PROPERTY = "PROPERTY";
    private static final String TIME_SLOT = "TIME_SLOT";
    private static final String TIER = "TIER";
    private static final String ALIGNABLE_ANNOTATION = "ALIGNABLE_ANNOTATION";
    private static final String REF_ANNOTATION = "REF_ANNOTATION";
    private static final String ANNOTATION_VALUE = "ANNOTATION_VALUE";

    //Campos de información del author y video
    //nota: verificar los campos a guardar
    private String author = "";
    private String date = "";
    private String format = "";
    private String version = "";
    private String xmlns_xsi = "";
    private String xsi_noNamespaceSchemaLocation = "";
    private String time_units = "";
    private String media_file = "";
    private String media_url = "";
    private String mime_type = "";
    private String relative_media_url = "";
    private String urn = "";
    private String last_used_annotation_id = "";

    //Variable para saber el tier en curso
    private String current_tier_id = "";

    //Banderas para obtener texto de los tags de xml
    private Boolean flag_last_used_annotation_id = false;
    private Boolean flag_urn = false;
    private Boolean flag_text = false;
    private String REF_ANNOTATION_TIER = "";
    private String REF_ANNOTATION_ID = "";

    //Listas de las traducciones
    //public List<Tier> tierList;


    //Objecto para almacenar los tiempo
    JsonObject jsonObjectTimeOrder = new JsonObject();
    JsonObject jsonObjectRefTimer = new JsonObject();
    JsonObject jsonObjectRefTranscription = new JsonObject();

    ParseHandlerMultiple(){

    }

    /**
     * Evento para el texto de una etiqueta, aqui se obtiene el texto de anotación y la agrega a la clase de tier
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(flag_urn){
            urn = new String(ch, start, length);
        }
        if(flag_last_used_annotation_id){
            last_used_annotation_id = new String(ch, start, length);
        }

        if(flag_text){
            if( current_tier_id.equals(pathSystem.TIER_MAIN) ||
                current_tier_id.equals(pathSystem.TIER_TRANSLATE) ||
                current_tier_id.equals(pathSystem.TIER_GlOSA_INDEX) ||
                current_tier_id.equals(pathSystem.TIER_GlOSA_INDEX_WORDS)){
                String annotation_value = new String(ch, start, length);

                String tierName = current_tier_id.toUpperCase().replaceAll(" ", "_");
                JsonObject REF_VALUES_TRANSCRIPTION = jsonObjectRefTranscription.getAsJsonObject(REF_ANNOTATION_TIER);
                REF_VALUES_TRANSCRIPTION.addProperty("ANNOTATION_VALUE_"+tierName, annotation_value);
                jsonObjectRefTranscription.add(REF_ANNOTATION_TIER, REF_VALUES_TRANSCRIPTION);
            }
        }
    }

    /**
     * Evento para el inicio de lectura de un documento
     * @throws SAXException
     */
    @Override
    public void startDocument() throws SAXException {
        //tierList = new ArrayList<>();
    }

    /**
     * Evento para el comienzo de cada etiqueta del xml, en este evento se guardan
     * los atributos de las etiquetas y se inicializan las banderas necesarias
     * @param uri
     * @param lName
     * @param qName
     * @param attr
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String lName, String qName, Attributes attr) throws SAXException {
        switch (qName){
            case ANNOTATION_DOCUMENT:
                author = attr.getValue("AUTHOR");
                date = attr.getValue("DATE");
                format = attr.getValue("FORMAT");
                version = attr.getValue("VERSION");
                xmlns_xsi = attr.getValue("xmlns_xsi");
                xsi_noNamespaceSchemaLocation = attr.getValue("xsi_noNamespaceSchemaLocation");
                break;
            case HEADER:
                time_units = attr.getValue("TIME_UNITS");
                media_file = attr.getValue("MEDIA_FILE");
                break;
            case MEDIA_DESCRIPTOR:
                media_url = attr.getValue("MEDIA_URL");
                mime_type = attr.getValue("MIME_TYPE");
                relative_media_url = attr.getValue("RELATIVE_MEDIA_URL");
                break;
            case PROPERTY:
                String type = attr.getValue("NAME");
                switch (type) {
                    case "URN":
                        flag_urn = true;
                        break;
                    case "lastUsedAnnotationId":
                        flag_last_used_annotation_id = true;
                        break;
                }
                break;
            case TIME_SLOT:
                String TIME_SLOT_ID = attr.getValue("TIME_SLOT_ID");
                String TIME_VALUE = attr.getValue("TIME_VALUE");
                jsonObjectTimeOrder.addProperty(TIME_SLOT_ID, TIME_VALUE);
                break;
            case TIER:
                //current_tier_id = attr.getValue("TIER_ID");
                String LINGUISTIC_TYPE_REF = attr.getValue("LINGUISTIC_TYPE_REF");
                String normalize = Normalizer.normalize(LINGUISTIC_TYPE_REF.toLowerCase(), Normalizer.Form.NFD);
                current_tier_id = normalize.replaceAll("[^\\p{ASCII}]", "");

                break;
            case ALIGNABLE_ANNOTATION:
                String ANNOTATION_ID = attr.getValue("ANNOTATION_ID");
                String TIME_SLOT_REF1 = attr.getValue("TIME_SLOT_REF1");
                String TIME_SLOT_REF2 = attr.getValue("TIME_SLOT_REF2");
                String TIME_VALUE1 = jsonObjectTimeOrder.get(TIME_SLOT_REF1).getAsString();
                String TIME_VALUE2 = jsonObjectTimeOrder.get(TIME_SLOT_REF2).getAsString();

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("TIME_SLOT_REF1", TIME_SLOT_REF1);
                jsonObject.addProperty("TIME_SLOT_REF2", TIME_SLOT_REF2);
                jsonObject.addProperty("TIME_VALUE1", TIME_VALUE1);
                jsonObject.addProperty("TIME_VALUE2", TIME_VALUE2);
                try{
                    jsonObject.addProperty("DIFF_TIME", Integer.parseInt(TIME_VALUE2) - Integer.parseInt(TIME_VALUE1));
                } catch (NumberFormatException e) {
                    System.out.println("ERROR EN TIEMPOS DEL EAF");
                    jsonObject.addProperty("DIFF_TIME", 0);
                }
                jsonObjectRefTimer.add(ANNOTATION_ID, jsonObject);

                break;
            case REF_ANNOTATION:
                if(current_tier_id.equals(pathSystem.TIER_MAIN)){
                    String REF_ANNOTATION_REF = attr.getValue("ANNOTATION_REF");
                    REF_ANNOTATION_ID = attr.getValue("ANNOTATION_ID");
                    REF_ANNOTATION_TIER = attr.getValue("ANNOTATION_ID");
                    String tierName = current_tier_id.toUpperCase().replaceAll(" ", "_");

                    JsonObject REF_VALUES = jsonObjectRefTimer.getAsJsonObject(REF_ANNOTATION_REF);
                    String REF_TIME_SLOT_REF1 = REF_VALUES.get("TIME_SLOT_REF1").getAsString();
                    String REF_TIME_SLOT_REF2 = REF_VALUES.get("TIME_SLOT_REF2").getAsString();
                    String REF_TIME_VALUE1 = REF_VALUES.get("TIME_VALUE1").getAsString();
                    String REF_TIME_VALUE2 = REF_VALUES.get("TIME_VALUE2").getAsString();

                    JsonObject jsonObjectTranscription= new JsonObject();
                    jsonObjectTranscription.addProperty("TIME_SLOT_REF1", REF_TIME_SLOT_REF1);
                    jsonObjectTranscription.addProperty("TIME_SLOT_REF2", REF_TIME_SLOT_REF2);
                    jsonObjectTranscription.addProperty("TIME_VALUE1", REF_TIME_VALUE1);
                    jsonObjectTranscription.addProperty("TIME_VALUE2", REF_TIME_VALUE2);
                    jsonObjectTranscription.addProperty("REF_ANNOTATION_ID_"+tierName, REF_ANNOTATION_ID);
                    jsonObjectTranscription.addProperty("REF_ANNOTATION_REF_ID_"+tierName, REF_ANNOTATION_REF);
                    jsonObjectRefTranscription.add(REF_ANNOTATION_ID, jsonObjectTranscription);
                }

                if( current_tier_id.equals(pathSystem.TIER_TRANSLATE) ||
                    current_tier_id.equals(pathSystem.TIER_GlOSA_INDEX) ||
                    current_tier_id.equals(pathSystem.TIER_GlOSA_INDEX_WORDS)){

                    String tierName = current_tier_id.toUpperCase().replaceAll(" ", "_");
                    REF_ANNOTATION_ID = attr.getValue("ANNOTATION_ID");
                    REF_ANNOTATION_TIER = attr.getValue("ANNOTATION_REF");

                    JsonObject REF_VALUES_TRANSCRIPTION = jsonObjectRefTranscription.getAsJsonObject(REF_ANNOTATION_TIER);
                    REF_VALUES_TRANSCRIPTION.addProperty("REF_ANNOTATION_ID_"+tierName, REF_ANNOTATION_ID);
                    REF_VALUES_TRANSCRIPTION.addProperty("REF_ANNOTATION_REF_ID_"+tierName, REF_ANNOTATION_TIER);
                    jsonObjectRefTranscription.add(REF_ANNOTATION_TIER, REF_VALUES_TRANSCRIPTION);
                }
                break;
            case ANNOTATION_VALUE:
                flag_text = true;
                break;
        }
    }

    /**
     * Evento para el fin de cada etiqueta del xml, en este se reinician las banderas
     * correspondientes al cierre de la etiqueta en curso
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName){
            case PROPERTY:
                flag_urn = false;
                flag_last_used_annotation_id = false;
                break;
            case ANNOTATION_VALUE:
                flag_text = false;
                break;
        }
    }

    /**
     * Regresa toda la lista de instancias de la clase Tier
     *
     * @return list<Tier> regresa la lista de anotaciones
     */
    public JsonObject getTier(){
        return jsonObjectRefTranscription;

    }

    /**
     * Regres el mime_type del multimedia del archivo eaf
     * @return String regresa el mime_type del multimedia del archivo eaf
     */
    public String getMimeType(){
        return mime_type;
    }
}
