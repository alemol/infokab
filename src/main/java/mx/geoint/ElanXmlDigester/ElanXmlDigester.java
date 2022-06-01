package mx.geoint.ElanXmlDigester;

import com.google.gson.Gson;
import mx.geoint.ParseXML.ParseXML;
import mx.geoint.ParseXML.Tier;

import java.util.List;

public class ElanXmlDigester {
    String filepath = "";
    List<Tier> getTier;

    public ElanXmlDigester(String eaf_path){
        filepath = eaf_path;
    }

    public void parse_tier(String tier_id, boolean save_text, boolean save_media){
        ParseXML parseXML = new ParseXML(filepath, tier_id);
        parseXML.read();
        getTier = parseXML.getTier();
    }

}
