package mx.geoint.ParseXML;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class ParseHandler extends DefaultHandler{
    private String tier_id;

    //VARIABLES PARA LOS STAGS IMPORTANTE DEL ARCHIVO .EAF
    private static final String ANNOTATION_DOCUMENT = "ANNOTATION_DOCUMENT";
    private static final String HEADER = "HEADER";
    private static final String MEDIA_DESCRIPTOR = "MEDIA_DESCRIPTOR";
    private static final String PROPERTY = "PROPERTY";
    private static final String TIME_SLOT = "TIME_SLOT";
    private static final String TIER = "TIER";
    private static final String ALIGNABLE_ANNOTATION = "ALIGNABLE_ANNOTATION";
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

    //Listas de las traducciones
    public List<Tier> tierList;

    //Objecto para almacenar los tiempo
    JsonObject jsonObjectTimeOrder = new JsonObject();

    /*
     * Inicializa el tipo de tier a obtner
     * @param tier_id tipo de tier a obtener
     **/
    ParseHandler(String tier_id){
        this.tier_id = tier_id;
    }

    /*
     * Evento para el texto de una etiqueta, aqui se obtiene el texto de anotación y la agrega a la clase de tier
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
            String annotation_value = new String(ch, start, length);
            if(tier_id.equals(current_tier_id)) {
                latestTier(tierList).setAnnotationValue(annotation_value);
            }
        }
    }

    /*
     * Evento para el inicio de lectura de un documento
     */
    @Override
    public void startDocument() throws SAXException {
        tierList = new ArrayList<>();
    }

    /*
     * Evento para el comienzo de cada etiqueta del xml, en este evento se guardan
     * los atributos del las etiquetas y se inicializan las banderas necesarias
    **/
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
                Tier new_tier = new Tier(ANNOTATION_ID, TIME_SLOT_REF1, TIME_VALUE1, TIME_SLOT_REF2, TIME_VALUE2);

                if(tier_id.equals(current_tier_id)) {
                    tierList.add(new_tier);
                }

                break;
            case ANNOTATION_VALUE:
                flag_text = true;
                break;
        }
    }

    /*
     * Evento para el fin de cada etiqueta del xml, en este se reinician las banders
     * correspondientes al cierre de la etiqueta en curso0
     **/
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

    /*
     * Obtiene el ultimo elemento de la lista de anotaciones
     * @params list<tier> una lista de tier
     * return Tier El ultimo elemento de la lista
     **/
    private Tier latestTier(List<Tier> tierList) {
        int latestTierIndex = tierList.size() - 1;
        return tierList.get(latestTierIndex);
    }

    /*
     * Obtiene toda  la lista de tier
     * return list<Tier> regresa la lista de anotaciones
     *
     **/
    public List<Tier> getTier(){
        return tierList;
    }
}
