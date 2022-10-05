package mx.geoint.UploadFiles;

import mx.geoint.ElanXmlDigester.ThreadElanXmlDigester;
import mx.geoint.pathSystem;
import mx.geoint.database.databaseController;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;


@Component
public class UploadFiles {
    //@Autowired
    //ThreadElanXmlDigester threadElanXmlDigester;
    private final ThreadElanXmlDigester threadElanXmlDigester;
    private final databaseController database;

    /**
     * Inicialización del thread
     */
    public UploadFiles(){
        database = new databaseController();
        threadElanXmlDigester = new ThreadElanXmlDigester();
        threadElanXmlDigester.start();
    }

    /**
     * Funcion principal para la carga de archivos
     *
     * @param eaf         MultipartFile, Archivo de anotaciones
     * @param multimedia  MultipartFile, Archivo de multimedia audio o video
     * @param uuid        String, Identificador de usuario
     * @param projectName String, Nombre del proyecto
     * @return boolean, respuesta de la carga de archivos
     * @throws IOException
     */
    public Number uploadFile(MultipartFile eaf, MultipartFile multimedia, MultipartFile autorizacion, String uuid, String projectName, String date, String hablantes, String ubicacion, String radio, String circleBounds) throws IOException {
        String baseProjectName = projectName.replace(" ", "_");
        String basePath = existDirectory(pathSystem.DIRECTORY_PROJECTS, uuid, baseProjectName);
        //int id_project = 0; //inicializa variable de id de de proyecto
        if(!saveFile(eaf, basePath, baseProjectName)){
            return pathSystem.NOT_UPLOAD_EAF_FILE;
        }

        if(!saveFile(multimedia, basePath, baseProjectName)){
            return pathSystem.NOT_UPLOAD_MULTIMEDIA_FILE;
        }

        if(autorizacion==null){
            System.out.println("sin archivo de autorizacion");
        }else{
            if(!saveFile(autorizacion, basePath, baseProjectName+"_autorizacion")){
                return pathSystem.NOT_UPLOAD_AUTORIZATION_FILE;
            }
        }


        int id_project = database.createProject(uuid, basePath, baseProjectName, date, hablantes, ubicacion, radio, circleBounds); //inserta un registro del proyecto en la base de datos
        System.out.println("ID de proyecto generado: "+id_project);
        if(id_project > 0){
            System.out.println("Proyecto guardado en base de datos");
            InitElanXmlDigester(eaf, multimedia, uuid, basePath, baseProjectName);
        }
        else{
            System.out.println("No se pudo guardar el proyecto en base de datos");
            return pathSystem.ERROR_DATABASE_SAVE_PROJECT;
        }

        return pathSystem.SUCCESS_UPLOAD;
    }

    /**
     * Inicializacion de los path's para el thread y la queue
     * @param eaf MultipartFile, Archivo de anotaciones
     * @param multimedia MultipartFile, Archivo de multimedia audio o video
     * @param uuid String, Identificador de usuario
     * @param projectName String, Nombre del proyecto
     */
    public void InitElanXmlDigester(MultipartFile eaf, MultipartFile multimedia, String uuid, String basePath, String projectName){
        System.out.println("InitElanXmlDigester.....");
        String extEaf = FilenameUtils.getExtension(eaf.getOriginalFilename());
        String extMultimedia = FilenameUtils.getExtension(multimedia.getOriginalFilename());

        String pathEaf = basePath+projectName+"."+extEaf;
        String pathMultimedia = basePath+projectName+"."+extMultimedia;
        threadElanXmlDigester.add(pathEaf, pathMultimedia, uuid);
        threadElanXmlDigester.activate();
    }

    /**
     *
     * @param file MultiparteFile, Archivo a subir o transferir
     * @param basePath String, ruta base para el archivo a subir o transferir
     * @param projectName String, Nombre del proyecto
     * @return
     * @throws IOException
     */
    public boolean saveFile(MultipartFile file, String basePath, String projectName) throws IOException {
        Date startDate = new Date();
        String name = file.getOriginalFilename();
        String ext  = FilenameUtils.getExtension(name);

        long size = file.getSize();
        Path path = Paths.get(basePath + projectName + "."+ ext);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        Date endDate = new Date();
        long difference_In_Time = endDate.getTime() - startDate.getTime();
        long difference_In_Seconds = (difference_In_Time / (1000)) % 60;
        long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
        System.out.println(" PATH: " + name + " SIZE: "+ size + " SAVE_TIME: "+ difference_In_Seconds +"s " + difference_In_Minutes+"m");

        return true;
    }

    /**
     *
     * @param pathDirectory String, directorio base
     * @param uuid String, indentificador del usuario
     * @param baseName String, Nombre base del archivo
     * @return
     */
    private String existDirectory(String pathDirectory, String uuid, String baseName){
        String currentDirectory = pathDirectory + uuid + "/"+baseName+"/";

        if(!Files.exists(Path.of(currentDirectory))){
            File newSubDirectory = new File(currentDirectory);
            newSubDirectory.mkdirs();
        }

        return currentDirectory;
    }
}
