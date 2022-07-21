package mx.geoint.ElanXmlDigester;

import com.google.gson.Gson;
import mx.geoint.FFmpeg.FFmpeg;
import mx.geoint.ParseXML.ParseXML;
import mx.geoint.ParseXML.Tier;
import mx.geoint.pathSystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.List;

public class ElanXmlDigester {
    String filepath = "";
    String uuid = "";
    List<Tier> getTier;

    /*
     * Inicializa el path del archivo a parsear
     * @param eaf_path path donde se localiza el archivo
     **/
    public ElanXmlDigester(String eaf_path, String uuid){
        String normalize = Normalizer.normalize(eaf_path, Normalizer.Form.NFD);
        this.uuid = uuid;
        filepath = normalize.replaceAll("[^\\p{ASCII}]", "");
    }

    /*
     * Obtiene las anotaciones por medio de ParseXML, realizar los cortes de audio y genera los archivos json con la
     * información necesaria para el indexado en lucene
     * @param
     *          tier_id:    el indentificador de tier a obtener
     *          save_text   bandera para iniciar el proceso de guardado del json
     *          save_media  bandera para iniciar el proceso de guardado de los fragmentos de audio
     **/
    public void parse_tier(String tier_id, boolean save_text, boolean save_media) throws IOException {
        ParseXML parseXML = new ParseXML(filepath, tier_id);
        parseXML.read();

        getTier = parseXML.getTier();
        if(save_media==true){
            FFmpeg ffmpeg = new FFmpeg(pathSystem.DIRECTORY_MULTIMEDIA);

            for (int i = 0; i< getTier.size();i++){
                Tier tier = getTier.get(i);

                String path = parseXML.getNameFile();
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
                    String path = parseXML.getNameFile();
                    saveText(tier, tier_id, path);
                }
            }
        }
    }

    /*
     * Se encarga de realizar los cortes de audio y guardarlo
     * @param
     *          ffmpeg:     El indentificador de tier a obtener
     *          tier        Informacion de la anotacion con sus tiempos
     *          tier_id     Tipo de anotacion a obtener
     *          path        Ruta del archivo o nombre del archivo //Definir
     *          type_path   Tipo de path sea audio o video
     **/
    public boolean saveMedia(FFmpeg ffmpeg, Tier tier, String tier_id, String path, String type_path){
        String file_name = format_name(tier, tier_id, path, type_path);
        boolean created = ffmpeg.cortador(path+"."+type_path,
                (Integer.parseInt(tier.TIME_VALUE1)/1000),
                (tier.DIFF_TIME/1000)+.5,
                file_name);
        return created;
    }

    /*
     * Se encarga de realizar los json y guardalos
     * @param
     *          tier        Informacion de la anotacion con sus tiempos
     *          tier_id     Tipo de anotacion a obtener
     *          path        Ruta del archivo o nombre del archivo //Definir
     **/
    public void saveText(Tier tier, String tier_id, String path) throws IOException {
        String file_name_json = format_name(tier, tier_id, path,"json");

        Gson gson = new Gson();
        String currentDirectory = existDirectory(pathSystem.DIRECTORY_FILES_JSON, uuid);
        FileWriter file = new FileWriter( currentDirectory + file_name_json);
        file.write(gson.toJson(tier));
        file.close();
    }

    /*
     * Se encarga de formatear el nombre a utilizar de los audio, videos y json
     * @param
     *          tier        Informacion de la anotacion con sus tiempos
     *          tier_id     Tipo de anotacion a obtener
     *          path        Ruta del archivo o nombre del archivo //Definir
     *          type_path   Tipo de path sea audio, video y/o json
     **/
    public String format_name(Tier tier, String tier_id, String path, String type_file){
        String name_file = String.format("%s_%s_%s_%s_%s_%s_%s.%s", tier.ANNOTATION_ID, tier_id, tier.TIME_SLOT_REF1, tier.TIME_SLOT_REF2, tier.TIME_VALUE1, tier.TIME_VALUE2, path, type_file);
        return name_file;
    }

    private String existDirectory(String pathDirectory, String uuid){
        String currentDirectory = pathDirectory + uuid + "/";

        if(!Files.exists(Path.of(currentDirectory))){
            if (!Files.exists(Path.of(pathDirectory))){
                File newDirectory = new File(pathDirectory);
                newDirectory.mkdir();
            }

            File newSubDirectory = new File(currentDirectory);
            newSubDirectory.mkdir();
        }

        return currentDirectory;
    }
}

    /*;*/