package mx.geoint.ElanXmlDigester;

import com.google.gson.Gson;
import mx.geoint.FFmpeg.FFmpeg;
import mx.geoint.ParseXML.ParseXML;
import mx.geoint.ParseXML.Tier;
import mx.geoint.VideoCutter.VideoCutter;
import mx.geoint.pathSystem;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.List;

public class ElanXmlDigester {
    String filepathEaf = "";
    String filepathMultimedia = "";
    String uuid = "";
    List<Tier> getTier;

    /*
     * Inicializa el path del archivo a parsear
     * @param eaf_path path donde se localiza el archivo
     **/
    public ElanXmlDigester(String eaf_path, String uuid){
        String normalize = Normalizer.normalize(eaf_path, Normalizer.Form.NFD);
        this.uuid = uuid;
        filepathEaf = normalize.replaceAll("[^\\p{ASCII}]", "");
    }

    /*
     * Inicializa el path del archivo a parsear
     * @param eaf_path path donde se localiza el archivo
     **/
    public ElanXmlDigester(String eaf_path, String multimedia_path, String uuid){
        this.uuid = uuid;
        //String normalize = Normalizer.normalize(eaf_path, Normalizer.Form.NFD);
        //filepath = normalize.replaceAll("[^\\p{ASCII}]", "");

        filepathEaf = eaf_path;
        filepathMultimedia = multimedia_path;
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
        ParseXML parseXML = new ParseXML(filepathEaf, tier_id);
        parseXML.read();

        getTier = parseXML.getTier();
        String baseNameEaf = FilenameUtils.getBaseName(filepathEaf);

        if(save_media==true){
            String type_path = getTypeMultimedia(filepathMultimedia);

            for (int i = 0; i<1; i++){
                Tier tier = getTier.get(i);

                if(type_path.equals("wav") || type_path.equals("mp4")){
                    boolean created = false;

                    switch (parseXML.getMimeType()){
                        case "audio/x-wav":
                            FFmpeg ffmpeg = new FFmpeg(filepathMultimedia);
                            created = saveAudio(ffmpeg, tier, tier_id, filepathMultimedia);
                            break;
                        case "video/mp4":
                            VideoCutter videoCutter = new VideoCutter();
                            created = saveVideo(videoCutter, tier, tier_id, filepathMultimedia);
                            break;
                    }

                    System.out.println(created);
                    if(created == true && save_text == true){
                        saveText(tier, tier_id, baseNameEaf);
                    }

                }
            }
        }else{
            if(save_text == true){
                for (int i = 0; i< getTier.size();i++) {
                    Tier tier = getTier.get(i);
                    saveText(tier, tier_id, baseNameEaf);
                }
            }
        }
    }

    /**
     *
     * @param videoCutter
     * @param tier
     * @param tier_id
     * @param source
     * @return
     */
    public boolean saveVideo(VideoCutter videoCutter, Tier tier, String tier_id, String source){
        String basePath = FilenameUtils.getPath(source)+"multimedia/";
        String path = FilenameUtils.getBaseName(source);
        String type_path = FilenameUtils.getExtension(source);

        String file_name = format_name(tier, tier_id, path, type_path);
        boolean created = videoCutter.Cutter(source,
                (Integer.parseInt(tier.TIME_VALUE1)/1000),
                (Integer.parseInt(tier.TIME_VALUE2)/1000),
                file_name);

        if(created){
            tier.setProjectName(path);
            tier.setPathMultimedia(basePath+file_name);
        }
        return created;
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
    public boolean saveAudio(FFmpeg ffmpeg, Tier tier, String tier_id, String source){
        String basePath = FilenameUtils.getPath(source)+"multimedia/";
        String path = FilenameUtils.getBaseName(source);
        String type_path = FilenameUtils.getExtension(source);

        String file_name = format_name(tier, tier_id, path, type_path);
        boolean created = ffmpeg.cortador(source,
                (Integer.parseInt(tier.TIME_VALUE1)/1000),
                (tier.DIFF_TIME/1000)+.5,
                file_name);

        if(created){
            tier.setProjectName(path);
            tier.setPathMultimedia(basePath+file_name);
        }
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

        String basePath = FilenameUtils.getPath(filepathEaf);
        String currentDirectory = existDirectory(basePath);

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

    /**
     *
     * @param pathDirectory
     * @return
     */
    private String existDirectory(String pathDirectory){
        String currentDirectory = pathDirectory + "file_to_index/";

        if(!Files.exists(Path.of(currentDirectory))){
            File newSubDirectory = new File(currentDirectory);
            newSubDirectory.mkdirs();
        }

        return currentDirectory;
    }

    /**
     *
     * @param filepathMultimedia
     * @return
     */
    private String getTypeMultimedia(String filepathMultimedia){
        String type_path = FilenameUtils.getExtension(filepathMultimedia);
        String normalize = Normalizer.normalize(type_path, Normalizer.Form.NFD);
        String type_path_normalize = normalize.replaceAll("[^\\p{ASCII}]", "");
        String type_path_loweCase = type_path_normalize.toLowerCase();
        return type_path_loweCase;
    }
}

    /*;*/