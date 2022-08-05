package mx.geoint.ElanXmlDigester;

import com.google.gson.Gson;
import mx.geoint.FFmpeg.FFmpeg;
import mx.geoint.ParseXML.ParseXML;
import mx.geoint.ParseXML.Tier;
import mx.geoint.VideoCutter.VideoCutter;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;

public class ElanXmlDigester {
    String filepathEaf = "";
    String filepathMultimedia = "";
    String uuid = "";
    List<Tier> getTier;

    /**
     * Inicializa el path del archivo a parsear
     *
     * @param eaf_path String, Ruta del archivo eaf
     * @param uuid     String, Identificador del usuario
     */
    public ElanXmlDigester(String eaf_path, String uuid) {
        String normalize = Normalizer.normalize(eaf_path, Normalizer.Form.NFD);
        this.uuid = uuid;
        filepathEaf = normalize.replaceAll("[^\\p{ASCII}]", "");
    }

    /**
     * Inicializa el path del archivo eaf y multimedia a parsear
     *
     * @param eaf_path        String, Ruta del archivo eaf
     * @param multimedia_path String, Ruta del archivo multimedia
     * @param uuid            String, Identificador del usuario
     */
    public ElanXmlDigester(String eaf_path, String multimedia_path, String uuid) {
        this.uuid = uuid;
        //String normalize = Normalizer.normalize(eaf_path, Normalizer.Form.NFD);
        //filepath = normalize.replaceAll("[^\\p{ASCII}]", "");

        filepathEaf = eaf_path;
        filepathMultimedia = multimedia_path;
    }

    /**
     * Obtiene las anotaciones por medio de ParseXML, realizar los cortes de audio y genera los archivos json con la
     * información necesaria para el indexado en lucene
     *
     * @param tier_id    String, Identificador de tier a obtener en el archivo eaf
     * @param save_text  boolean, bandera para iniciar el proceso de guardado del json
     * @param save_media boolean, bandera para iniciar el proceso de guardado de los fragmentos del multimedia
     * @throws IOException
     */
    public void parse_tier(String tier_id, boolean save_text, boolean save_media) throws IOException {
        ParseXML parseXML = new ParseXML(filepathEaf, tier_id);

        parseXML.read();

        getTier = parseXML.getTier();
        String baseNameEaf = FilenameUtils.getBaseName(filepathEaf);
        String type_path = getTypeMultimedia(filepathMultimedia);
        System.out.println("type_path: " + type_path);
        double s[] = new double[getTier.size()];
        double e[] = new double[getTier.size()];
        String name[] = new String[getTier.size()];
        if (save_media == true) {

            if (type_path.equals("wav")) {
                for (int i = 0; i < getTier.size(); i++) {
                    Tier tier = getTier.get(i);
                    boolean created = false;
                    FFmpeg ffmpeg = new FFmpeg(filepathMultimedia);
                    created = saveAudio(ffmpeg, tier, tier_id, filepathMultimedia);
                    //VideoCutter videoCutter = new VideoCutter();
                    //created = saveVideo(videoCutter, tier, tier_id, filepathMultimedia);

                    System.out.println(created);
                    if (created == true && save_text == true) {
                        saveText(tier, tier_id, baseNameEaf);
                    }
                }
            }
            if (type_path.equals("mp4")) {
                System.out.println("video");
                for (int i = 0; i < 10; i++) {
                    Tier tier = getTier.get(i);
                    String path = FilenameUtils.getBaseName(filepathMultimedia);
                    String extension = FilenameUtils.getExtension(filepathMultimedia);
                    s[i] = Double.parseDouble(tier.TIME_VALUE1) / 1000;
                    e[i] = Double.parseDouble(tier.TIME_VALUE2) / 1000;
                    name[i] = format_name(tier, tier_id, path, extension);

                }
                VideoCutter videoCutter = new VideoCutter();
                System.out.println(s.length);
                HashMap<String, Boolean> CreatedList = saveVideo(videoCutter,s,e,name,filepathMultimedia);
                System.out.println(CreatedList);
            }


        } else {
            if (save_text == true) {
                for (int i = 0; i < getTier.size(); i++) {
                    Tier tier = getTier.get(i);
                    saveText(tier, tier_id, baseNameEaf);
                }
            }
        }

    }

    /**
     * Se encarga de realizar los cortes de video y guardarlo
     *
     * @param videoCutter VideoCutter, Una instancia de la clase videoCutter
     * @param tier        Tier, Instancia de la clase tier
     * @param tier_id     String, Identificador de tier a obtener en el archivo eaf
     * @param source      String, ruta del multimedia
     * @return
     */
    public HashMap<String, Boolean> saveVideo(VideoCutter videoCutter, double[] s, double[] e, String[] name, String source){
        String basePath = FilenameUtils.getPath(source) + "multimedia/";
        String path = FilenameUtils.getBaseName(source);
        String type_path = FilenameUtils.getExtension(source);
        //boolean created = videoCutter.Cutter();
        HashMap<String, Boolean> CreatedList = videoCutter.Cutter2(s, e, source,name);
        return CreatedList;
    }
    /*public boolean saveVideo(VideoCutter videoCutter, Tier tier, String tier_id, String source) {
        String basePath = FilenameUtils.getPath(source) + "multimedia/";
        String path = FilenameUtils.getBaseName(source);
        String type_path = FilenameUtils.getExtension(source);

        String file_name = format_name(tier, tier_id, path, type_path);
        boolean created = videoCutter.Cutter(source,
                (Integer.parseInt(tier.TIME_VALUE1) / 1000),
                (Integer.parseInt(tier.TIME_VALUE2) / 1000),
                file_name);

        if (created) {
            tier.setProjectName(path);
            tier.setMediaPath(basePath + file_name);
            tier.setOriginalMediaPath(source);
        }
        return created;
    }*/

    /**
     * Se encarga de realizar los cortes de audio y guardarlo
     *
     * @param ffmpeg  FFmpef, Una instancia de la clase FFmpeg
     * @param tier    Tier, Instancia de la clase tier
     * @param tier_id String, Identificador de tier a obtener en el archivo eaf
     * @param source  String, ruta del multimedia
     * @return
     */
    public boolean saveAudio(FFmpeg ffmpeg, Tier tier, String tier_id, String source) {
        String basePath = FilenameUtils.getPath(source) + "multimedia/";
        String path = FilenameUtils.getBaseName(source);
        String type_path = FilenameUtils.getExtension(source);

        String file_name = format_name(tier, tier_id, path, type_path);
        boolean created = ffmpeg.cortador(source,
                (Integer.parseInt(tier.TIME_VALUE1) / 1000),
                (tier.DIFF_TIME / 1000) + .5,
                file_name);

        if (created) {
            tier.setProjectName(path);
            tier.setMediaPath(basePath + file_name);
            tier.setOriginalMediaPath(source);
        }
        return created;
    }

    /**
     * Se encarga de realizar los json y guardalos
     *
     * @param tier    Tier, Instancia de la clase tier
     * @param tier_id String, Identificador de tier a obtener en el archivo eaf
     * @param path    String, Nombre del archivo
     * @throws IOException
     */
    public void saveText(Tier tier, String tier_id, String path) throws IOException {
        String file_name_json = format_name(tier, tier_id, path, "json");
        System.out.println(tier);
        System.out.println(tier_id);
        System.out.println(path);
        System.out.println(file_name_json);
        Gson gson = new Gson();

        String basePath = FilenameUtils.getPath(filepathEaf);
        String currentDirectory = existDirectory(basePath);

        FileWriter file = new FileWriter(currentDirectory + file_name_json);
        file.write(gson.toJson(tier));
        file.close();
    }

    /**
     * Se encarga de formatear el nombre a utilizar de los audio, videos y json
     *
     * @param tier      Tier, Instancia de la clase tier
     * @param tier_id   String, Identificador de tier a obtener en el archivo eaf
     * @param path      String, Nombre del archivo
     * @param type_file String, extension del archivo
     * @return
     */
    public String format_name(Tier tier, String tier_id, String path, String type_file) {
        String name_file = String.format("%s_%s_%s_%s_%s_%s_%s.%s", tier.ANNOTATION_ID, tier_id, tier.TIME_SLOT_REF1, tier.TIME_SLOT_REF2, tier.TIME_VALUE1, tier.TIME_VALUE2, path, type_file);
        return name_file;
    }

    /**
     * Regresa la ruta del directorio a crear o existente
     *
     * @param pathDirectory String, Directorio a crear
     * @return
     */
    private String existDirectory(String pathDirectory) {
        String currentDirectory = pathDirectory + "file_to_index/";

        if (!Files.exists(Path.of(currentDirectory))) {
            File newSubDirectory = new File(currentDirectory);
            newSubDirectory.mkdirs();
        }

        return currentDirectory;
    }

    /**
     * regresa la extension del archivo multimedia normalizada y en minusculas
     *
     * @param filepathMultimedia String, tipo de extension del multimedia
     * @return
     */
    private String getTypeMultimedia(String filepathMultimedia) {
        String type_path = FilenameUtils.getExtension(filepathMultimedia);
        String normalize = Normalizer.normalize(type_path, Normalizer.Form.NFD);
        String type_path_normalize = normalize.replaceAll("[^\\p{ASCII}]", "");
        String type_path_loweCase = type_path_normalize.toLowerCase();
        return type_path_loweCase;
    }

    /**
     * @return String, Identificador del usuario
     */
    public String getUUID() {
        return uuid;
    }

    /**
     * @return String, Directorio donde se encuentran los json
     */
    public String basePathJsonFiles() {
        String basePath = FilenameUtils.getPath(filepathEaf);
        return existDirectory(basePath);
    }
}