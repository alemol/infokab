package mx.geoint.Controllers.ElanXmlDigester;

import com.google.gson.Gson;
import mx.geoint.Controllers.FFmpeg.FFmpeg;
import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Controllers.ParseXML.ParseXML;
import mx.geoint.Model.ParseXML.Tier;
import mx.geoint.Controllers.VideoCutter.VideoCutter;
import mx.geoint.Database.DBProjects;
import mx.geoint.Database.DBReports;
import mx.geoint.pathSystem;
import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ElanXmlDigester {
    private Logger logger = new Logger();
    String filepathEaf = "";
    String filepathMultimedia = "";
    String uuid = "";
    int projectID;
    public List<Tier> getTier;
    private DBReports dbReports;
    private DBProjects dbProjects;

    /**
     * Inicializa el path del archivo a parsear
     * @param eaf_path String, Ruta del archivo eaf
     * @param uuid String, Identificador del usuario
     */
    public ElanXmlDigester(String eaf_path, String uuid){
        String normalize = Normalizer.normalize(eaf_path, Normalizer.Form.NFD);
        this.uuid = uuid;
        filepathEaf = normalize.replaceAll("[^\\p{ASCII}]", "");
        dbReports = new DBReports();
        dbProjects = new DBProjects();
    }

    /**
     * Inicializa el path del archivo eaf y multimedia a parsear
     * @param eaf_path String, Ruta del archivo eaf
     * @param multimedia_path String, Ruta del archivo multimedia
     * @param uuid String, Identificador del usuario
     */
    public ElanXmlDigester(String eaf_path, String multimedia_path, String uuid){
        this.uuid = uuid;
        //String normalize = Normalizer.normalize(eaf_path, Normalizer.Form.NFD);
        //filepath = normalize.replaceAll("[^\\p{ASCII}]", "");

        filepathEaf = eaf_path;
        filepathMultimedia = multimedia_path;
        dbReports = new DBReports();
        dbProjects = new DBProjects();
    }

    /**
     * Inicializa el path del archivo eaf y multimedia a parsear
     * @param eaf_path String, Ruta del archivo eaf
     * @param multimedia_path String, Ruta del archivo multimedia
     * @param uuid String, Identificador del usuario
     */
    public ElanXmlDigester(String eaf_path, String multimedia_path, String uuid, int projectID){
        this.uuid = uuid;
        filepathEaf = eaf_path;
        filepathMultimedia = multimedia_path;
        this.projectID = projectID;
        dbReports = new DBReports();
        dbProjects = new DBProjects();
    }

    /**
     * Analisis del archivo eaf
     * @throws SQLException
     */
    public void validateElanXmlDigester() throws SQLException, ParserConfigurationException, IOException, SAXException {
        ParseXML parseXML = new ParseXML(filepathEaf);
        parseXML.read();

        Map<String, List<Tier>> tiersList = parseXML.getTiers();

        List<Integer> tierCount = new ArrayList<>();
        Boolean error_tier = false;

        for (var entry : tiersList.entrySet()){
            String key = entry.getKey();

            if(key.equals(pathSystem.TIER_MAIN)){
                error_tier = true;
                dbProjects.setProjectAnnotationsCounter(projectID, entry.getValue().size());
            }

            List<Tier> tierList = entry.getValue();
            tierCount.add(tierList.size());
        }

        if(!error_tier){
            dbProjects.setProjectAnnotationsCounter(projectID, 0);
            dbReports.newRegister(projectID, "TIER PRINCIPAL", "No se encontro el tier principal","document", "", "");
        }

        HashSet<Integer> set = new HashSet<Integer>(tierCount);
        if(set.size() > 1){
            dbReports.newRegister(projectID, "NO COINDICEN EL NUMERO DE ANOTACIONES", "","document", "", "");
        }

        if(set.size() == 1) {
            for (var entry : tiersList.entrySet()){
                List<Tier> tierList = entry.getValue();

                if(tierList.isEmpty()){
                    dbReports.newRegister(projectID, "Tier Vacio", "La capa "+entry.getKey()+" no contiene anotaciones","document", "", "");
                    continue;
                }

                for (var register : tierList){
                    if(register.DIFF_TIME == 0){
                        dbReports.newRegister(projectID, "TIEMPO DE CORTE CERO", "La anotación "+register.ANNOTATION_ID,"document", "", "");
                    }
                }
            }
        }
    }

    /**
     * Obtiene las anotaciones por medio de ParseXML, realizar los cortes de audio y genera los archivos json con la
     * información necesaria para el indexado en lucene
     * @param tier_id String, Identificador de tier a obtener en el archivo eaf
     * @param save_text boolean, bandera para iniciar el proceso de guardado del json
     * @param save_media boolean, bandera para iniciar el proceso de guardado de los fragmentos del multimedia
     * @throws IOException
     */
    public void parse_tier(String tier_id, boolean save_text, boolean save_media) throws ParserConfigurationException, SAXException, IOException {
        ParseXML parseXML = new ParseXML(filepathEaf, tier_id);
        parseXML.read();

        getTier = parseXML.getTier();
        String baseNameEaf = FilenameUtils.getBaseName(filepathEaf);

        if(save_media==true){
            String type_path = getTypeMultimedia(filepathMultimedia);

            for (int i = 0; i< getTier.size(); i++){
                Tier tier = getTier.get(i);

                if(type_path.equals("wav") || type_path.equals("mp4")){
                    boolean created = false;

                    switch (parseXML.getMimeType()){
                        case "audio/x-wav":
                            FFmpeg ffmpeg = new FFmpeg(filepathMultimedia);
                            try{
                                created = saveAudio(ffmpeg, tier, tier_id, filepathMultimedia);
                            } catch (IOException e) {
                                logger.appendToFile(e);
                            }
                            break;
                        case "video/mp4":
                            VideoCutter videoCutter = new VideoCutter(filepathMultimedia);
                            try{
                                created = saveVideo(videoCutter, tier, tier_id, filepathMultimedia);
                            } catch (IOException e) {
                                logger.appendToFile(e);
                            }
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
     * Se encarga de realizar los cortes de video y guardarlo
     * @param videoCutter VideoCutter, Una instancia de la clase videoCutter
     * @param tier Tier, Instancia de la clase tier
     * @param tier_id String, Identificador de tier a obtener en el archivo eaf
     * @param source String, ruta del multimedia
     * @return
     */
    public boolean saveVideo(VideoCutter videoCutter, Tier tier, String tier_id, String source) throws IOException {
        String basePath = FilenameUtils.getPath(source)+"multimedia/";
        String path = FilenameUtils.getBaseName(source);
        String type_path = FilenameUtils.getExtension(source);

        String file_name = format_name(tier, tier_id, path, type_path);

        boolean created = videoCutter.cortador(source,
                (Integer.parseInt(tier.TIME_VALUE1)/1000),
                (tier.DIFF_TIME/1000)+.5,
                file_name);

        if(created){
            tier.setProjectName(path);
            tier.setMediaPath(basePath+file_name);
            tier.setOriginalMediaPath(source);
        }

        return created;
    }

    /**
     * Se encarga de realizar los cortes de audio y guardarlo
     * @param ffmpeg FFmpef, Una instancia de la clase FFmpeg
     * @param tier Tier, Instancia de la clase tier
     * @param tier_id String, Identificador de tier a obtener en el archivo eaf
     * @param source String, ruta del multimedia
     * @return
     */
    public boolean saveAudio(FFmpeg ffmpeg, Tier tier, String tier_id, String source) throws IOException {
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
            tier.setMediaPath(basePath+file_name);
            tier.setOriginalMediaPath(source);
        }

        return created;
    }

    /**
     * Se encarga de realizar los json y guardalos
     * @param tier Tier, Instancia de la clase tier
     * @param tier_id String, Identificador de tier a obtener en el archivo eaf
     * @param path String, Nombre del archivo
     * @throws IOException
     */
    public void saveText(Tier tier, String tier_id, String path) throws IOException {
        String file_name_json = format_name(tier, tier_id, path,"json");
        Gson gson = new Gson();

        String basePath = FilenameUtils.getPath(filepathEaf);
        String currentDirectory = "";

        if(tier_id == pathSystem.TIER_MAIN) {
            currentDirectory = existDirectory(basePath + "/"+pathSystem.INDEX_LANGUAJE_MAYA+"/");
        }

        if(tier_id == pathSystem.TIER_TRANSLATE){
            currentDirectory = existDirectory(basePath+ "/"+pathSystem.INDEX_LANGUAJE_SPANISH+"/");
        }

        if(tier_id == pathSystem.TIER_GlOSA_INDEX){
            currentDirectory = existDirectory(basePath+ "/"+pathSystem.INDEX_LANGUAJE_GLOSA+"/");
        }

        if(tier_id == pathSystem.TIER_GlOSA_INDEX_WORDS){
            currentDirectory = existDirectory(basePath+ "/"+pathSystem.INDEX_LANGUAJE_GLOSA_WORDS+"/");
        }

        FileWriter file = new FileWriter( currentDirectory + file_name_json);
        file.write(gson.toJson(tier));
        file.close();
    }

    /**
     * Se encarga de formatear el nombre a utilizar de los audio, videos y json
     * @param tier Tier, Instancia de la clase tier
     * @param tier_id String, Identificador de tier a obtener en el archivo eaf
     * @param path String, Nombre del archivo
     * @param type_file String, extension del archivo
     * @return
     */
    public String format_name(Tier tier, String tier_id, String path, String type_file){
        //String name_file = String.format("%s_%s_%s_%s_%s_%s_%s.%s", tier.ANNOTATION_ID, tier_id, tier.TIME_SLOT_REF1, tier.TIME_SLOT_REF2, tier.TIME_VALUE1, tier.TIME_VALUE2, path, type_file);
        //String name_file = String.format("%s_%s_%s_%s_%s_%s.%s", tier.ANNOTATION_ID, tier.TIME_SLOT_REF1, tier.TIME_SLOT_REF2, tier.TIME_VALUE1, tier.TIME_VALUE2, path, type_file);
        String name_file = String.format("%s_%s_%s_%s_%s.%s", tier.TIME_SLOT_REF1, tier.TIME_SLOT_REF2, tier.TIME_VALUE1, tier.TIME_VALUE2, path, type_file);
        return name_file;
    }

    /**
     *  Regresa la ruta del directorio a crear o existente
     * @param pathDirectory String, Directorio a crear
     * @return
     */
    private String existDirectory(String pathDirectory){
        String currentDirectory = pathDirectory;

        if(!Files.exists(Path.of(currentDirectory))){
            File newSubDirectory = new File(currentDirectory);
            newSubDirectory.mkdirs();
        }

        return currentDirectory;
    }

    /**
     *  regresa la extension del archivo multimedia normalizada y en minusculas
     * @param filepathMultimedia String, tipo de extension del multimedia
     * @return
     */
    private String getTypeMultimedia(String filepathMultimedia){
        String type_path = FilenameUtils.getExtension(filepathMultimedia);
        String normalize = Normalizer.normalize(type_path, Normalizer.Form.NFD);
        String type_path_normalize = normalize.replaceAll("[^\\p{ASCII}]", "");
        String type_path_loweCase = type_path_normalize.toLowerCase();
        return type_path_loweCase;
    }

    /**
     *
     * @return String, Identificador del usuario
     */
    public String getUUID(){
        return uuid;
    }

    /**
     *
     * @return String, Directorio donde se encuentran los json
     */
    public String basePathJsonFiles(){
        String basePath = FilenameUtils.getPath(filepathEaf);
        return existDirectory(basePath);
    }
}