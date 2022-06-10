package mx.geoint.ElanXmlDigester;

import com.google.gson.Gson;
import mx.geoint.FFmpeg.FFmpeg;
import mx.geoint.ParseXML.ParseXML;
import mx.geoint.ParseXML.Tier;
import mx.geoint.pathSystem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ElanXmlDigester {
    String filepath = "";
    List<Tier> getTier;

    public ElanXmlDigester(String eaf_path){
        filepath = eaf_path;
    }

    public void parse_tier(String tier_id, boolean save_text, boolean save_media) throws IOException {
        ParseXML parseXML = new ParseXML(filepath, tier_id);
        parseXML.read();

        getTier = parseXML.getTier();
        if(save_media==true){
            FFmpeg ffmpeg = new FFmpeg(pathSystem.DIRECTORY_MULTIMEDIA);

            for (int i = 0; i< getTier.size();i++){
                Tier tier = getTier.get(i);
                String path = "eligio_uikab_mena";
                String type_path = "wav";
                boolean created = saveMedia(ffmpeg, tier, tier_id, path, type_path);
                System.out.println(created);
                if(created == true && save_text == true){
                    saveText(tier, tier_id, path);
                }
            }
        }else{
            if(save_text == true){
                for (int i = 0; i< getTier.size();i++) {
                    Tier tier = getTier.get(i);
                    String path = "eligio_uikab_mena";
                    saveText(tier, tier_id, path);
                }
            }
        }
    }

    public boolean saveMedia(FFmpeg ffmpeg, Tier tier, String tier_id, String path, String type_path){
        String file_name = format_name(tier, tier_id, path, type_path);
        boolean created = ffmpeg.cortador(path+"."+type_path,
                (Integer.parseInt(tier.TIME_VALUE1)/1000),
                (tier.DIFF_TIME/1000)+.5,
                file_name);
        return created;
    }

    public void saveText(Tier tier, String tier_id, String path) throws IOException {
        String file_name_json = format_name(tier, tier_id, path,"json");

        Gson gson = new Gson();
        FileWriter file = new FileWriter(pathSystem.DIRECTORY_FILES_JSON + file_name_json);
        file.write(gson.toJson(tier));
        file.close();
    }

    public String format_name(Tier tier, String tier_id, String path, String type_file){
        String name_file = String.format("%s_%s_%s_%s_%s_%s_%s.%s", tier.ANNOTATION_ID, tier_id, tier.TIME_SLOT_REF1, tier.TIME_SLOT_REF2, tier.TIME_VALUE1, tier.TIME_VALUE2, path, type_file);
        System.out.println(name_file);
        return name_file;
    }
}

    /*;*/