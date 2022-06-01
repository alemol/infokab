package mx.geoint.ElanXmlDigester;

import com.google.gson.Gson;
import mx.geoint.FFmpeg.FFmpeg;
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
        FFmpeg ffmpeg = new FFmpeg("src/main/resources/");
        getTier = parseXML.getTier();
        for (int i = 0; i< getTier.size();i++){
            Tier tier = getTier.get(i);
            System.out.println(getTier.get(i).ANNOTATION_ID);
            boolean creado = ffmpeg.cortador("eligio_uikab_mena.wav",
                    (Integer.parseInt(tier.TIME_VALUE1)/1000),
                    (tier.DIFF_TIME/1000)+.5,
                    tier.ANNOTATION_ID+"_"+tier_id+"_"+tier.TIME_SLOT_REF1+"_"+tier.TIME_SLOT_REF2+"_"+tier.TIME_VALUE1+"_"+tier.TIME_VALUE2+"_eligio_uikab_mena.wav");
            System.out.println(creado);
        }
    }

}

    /*Gson gson = new Gson();
    FileWriter file = new
            FileWriter("path/"+tier.ANNOTATION_ID+".json");
            file.write(gson.toJson(tier));
                    file.close();*/