package mx.geoint.ParseXML;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class ParseHandler extends DefaultHandler{
    private String tier_id;

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

    private String current_tier_id = "";

    //Banderas para obtener texto de xml
    private Boolean flag_last_used_annotation_id = false;
    private Boolean flag_urn = false;
    private Boolean flag_text = false;

    //Listas para los tier
    public List<Tier> tierList;

    //Objecto con los tiempos de time order
    JsonObject jsonObjectTimeOrder = new JsonObject();

    ParseHandler(String tier_id){
        this.tier_id = tier_id;
    }

    /*
        CHARACTERS obtiene el texto del xml
    **/
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

    @Override
    public void startDocument() throws SAXException {
        tierList = new ArrayList<>();
    }

    /*
    startElement: se invoca para cada comienzo de elemento de xml
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
                current_tier_id = attr.getValue("TIER_ID");
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
        endElement: se invoca para fin de elemento de xml
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

    private Tier latestTier(List<Tier> tierList) {
        int latestTierIndex = tierList.size() - 1;
        return tierList.get(latestTierIndex);
    }

    public List<Tier> getTier(){
        return tierList;
    }
}
