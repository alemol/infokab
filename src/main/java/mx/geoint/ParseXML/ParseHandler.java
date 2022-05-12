package mx.geoint.ParseXML;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class ParseHandler extends DefaultHandler{
    //Campos de XML
    private static final String ANNOTATION_DOCUMENT = "ANNOTATION_DOCUMENT";
    private static final String HEADER = "HEADER";
    private static final String MEDIA_DESCRIPTOR = "MEDIA_DESCRIPTOR";
    private static final String PROPERTY = "PROPERTY";
    private static final String TIME_SLOT = "TIME_SLOT";
    private static final String TIER = "TIER";
    private static final String ALIGNABLE_ANNOTATION = "ALIGNABLE_ANNOTATION";
    private static final String ANNOTATION_VALUE = "ANNOTATION_VALUE";

    //Campos de información del author y video
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

    private String tier_id = "";

    //Banderas para obtener texto de xml
    private Boolean flag_last_used_annotation_id = false;
    private Boolean flag_urn = false;
    private Boolean flag_text = false;

    //Listas para los tier
    public List<Tier> transcripcion;
    public List<Tier> traduccion;
    public List<Tier> glosado;
    public List<Tier> morfemas;

    //Objecto con los tiempos de time order
    JsonObject jsonObjectTimeOrder = new JsonObject();

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
            switch (tier_id){
                case "Transcripción":
                    latestTier(transcripcion).setAnnotationValue(annotation_value);
                    break;
                case "Traducción":
                    latestTier(traduccion).setAnnotationValue(annotation_value);
                    break;
                case "Glosado":
                    latestTier(glosado).setAnnotationValue(annotation_value);
                    break;
                case "Morfemas":
                    latestTier(morfemas).setAnnotationValue(annotation_value);
                    break;
            }
        }
    }

    @Override
    public void startDocument() throws SAXException {
        transcripcion = new ArrayList<>();
        traduccion = new ArrayList<>();
        glosado = new ArrayList<>();
        morfemas = new ArrayList<>();
    }

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
                tier_id = attr.getValue("TIER_ID");
                break;
            case ALIGNABLE_ANNOTATION:
                String ANNOTATION_ID = attr.getValue("ANNOTATION_ID");
                String TIME_SLOT_REF1 = attr.getValue("TIME_SLOT_REF1");
                String TIME_SLOT_REF2 = attr.getValue("TIME_SLOT_REF2");
                String TIME_VALUE1 = jsonObjectTimeOrder.get(TIME_SLOT_REF1).getAsString();
                String TIME_VALUE2 = jsonObjectTimeOrder.get(TIME_SLOT_REF2).getAsString();
                Tier new_tier = new Tier(ANNOTATION_ID, TIME_SLOT_REF1, TIME_VALUE1, TIME_SLOT_REF2, TIME_VALUE2);

                switch (tier_id){
                    case "Transcripción":
                        transcripcion.add(new_tier);
                        break;
                    case "Traducción":
                        traduccion.add(new_tier);
                        break;
                    case "Glosado":
                        glosado.add(new_tier);
                        break;
                    case "Morfemas":
                        morfemas.add(new_tier);
                        break;
                    default:
                        System.out.println("NO FOUND CATEGORY");
                }

                break;
            case ANNOTATION_VALUE:
                flag_text = true;
                break;
        }
    }

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

    private Tier latestTier(List<Tier> tierList) {
        int latestTierIndex = tierList.size() - 1;
        return tierList.get(latestTierIndex);
    }

    public List<Tier> getTranscripcion(){
        return transcripcion;
    }

    public List<Tier> getTraduccion(){
        return traduccion;
    }
    public List<Tier> getGlosado(){
        return glosado;
    }
    public List<Tier> getMorfemas(){
        return morfemas;
    }

    public void printTranscripcion(){
        Gson gson = new Gson();
        String json = gson.toJson(transcripcion);
        System.out.println("Traducción: " + json);
    }
    public void printTraduccion(){
        Gson gson = new Gson();
        String json = gson.toJson(traduccion);
        System.out.println("Traducción: " + json);
    }

    public void printGlosado(){
        Gson gson = new Gson();
        String json = gson.toJson(glosado);
        System.out.println("Glosado: " + json);
    }

    public void printMorfemas(){
        Gson gson = new Gson();
        String json = gson.toJson(morfemas);
        System.out.println("Morfemas: " + json);
    }
}
