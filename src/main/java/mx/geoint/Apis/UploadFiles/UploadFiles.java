package mx.geoint.Apis.UploadFiles;

import mx.geoint.Controllers.ElanXmlDigester.ThreadElanXmlDigester;
import mx.geoint.Controllers.ElanXmlDigester.ThreadValidateElanXmlDigester;
import mx.geoint.database.DBAnnotations;
import mx.geoint.database.DBProjects;
import mx.geoint.database.DBReports;
import mx.geoint.database.DBUsers;
import mx.geoint.pathSystem;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;


@Component
public class UploadFiles {
    //@Autowired
    //ThreadElanXmlDigester threadElanXmlDigester;
    private final ThreadElanXmlDigester threadElanXmlDigester;
    private final ThreadValidateElanXmlDigester threadValidateElanXmlDigester;
    private final DBUsers database;
    private final DBReports dbReports;
    private final DBAnnotations dbAnnotations;
    private final DBProjects dbProjects;

    /**
     * Inicialización del thread
     */
    public UploadFiles(){
        database = new DBUsers();
        dbReports = new DBReports();
        dbAnnotations = new DBAnnotations();
        dbProjects = new DBProjects();
        threadElanXmlDigester = new ThreadElanXmlDigester();
        threadElanXmlDigester.start();

        threadValidateElanXmlDigester = new ThreadValidateElanXmlDigester();
        threadValidateElanXmlDigester.start();
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
    public Number uploadFile(MultipartFile eaf, MultipartFile multimedia, MultipartFile autorizacion, MultipartFile[] images, String uuid, String projectName, String date, String hablantes, String ubicacion, String radio, String circleBounds) throws IOException, SQLException {
        String baseProjectName = projectName.replace(" ", "_");
        String basePath = existDirectory(pathSystem.DIRECTORY_PROJECTS, uuid, baseProjectName);

        if(!saveFile(eaf, basePath, baseProjectName)){
            return pathSystem.NOT_UPLOAD_EAF_FILE;
        }

        if(!saveFile(eaf, basePath, baseProjectName + "_ORIGINAL")){
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
        if(images==null){
            System.out.println("sin archivo de imagenes");
        }else{
            int index = 0;
            String ImageFolder = basePath+"Images/";
            System.out.println(ImageFolder);
            File newSubDirectory = new File(ImageFolder);
            newSubDirectory.mkdirs();
            for(MultipartFile file : images) {
                //System.out.println("algo2 " + file.getOriginalFilename());
                if(!saveFile(file, ImageFolder, baseProjectName+"_image"+(index++))){
                    return pathSystem.NOT_UPLOAD_AUTORIZATION_FILE;
                }
            }

        }

        int id_project = dbProjects.createProject(uuid, basePath, baseProjectName, date, hablantes, ubicacion, radio, circleBounds); //inserta un registro del proyecto en la base de datos

        System.out.println("ID de proyecto generado: "+id_project);
        if(id_project > 0){
            System.out.println("Proyecto guardado en base de datos");
            //InitElanXmlDigester(eaf, multimedia, uuid, basePath, baseProjectName, id_project);
            InitValidateElanXmlDigester(eaf, uuid, basePath, baseProjectName, (Integer) id_project);
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
    public void InitElanXmlDigester(MultipartFile eaf, MultipartFile multimedia, String uuid, String basePath, String projectName, int projectID){
        System.out.println("InitElanXmlDigester.....");
        String extEaf = FilenameUtils.getExtension(eaf.getOriginalFilename());
        String extMultimedia = FilenameUtils.getExtension(multimedia.getOriginalFilename());

        String pathEaf = basePath+projectName+"."+extEaf;
        String pathMultimedia = basePath+projectName+"."+extMultimedia;
        threadElanXmlDigester.add(pathEaf, pathMultimedia, uuid, projectID);
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

    /*private String existDirectoryImages(String pathDirectory, String uuid, String baseName){
        String currentDirectory = pathDirectory + uuid + "/"+baseName+"/";

        if(!Files.exists(Path.of(currentDirectory))){
            File newSubDirectory = new File(currentDirectory);
            newSubDirectory.mkdirs();
        }

        return currentDirectory;
    }*/

    public Number updateEaf(MultipartFile eaf, String projectName, String uuid, int id_project) throws IOException, SQLException {
        String baseProjectName = projectName.replace(" ", "_");
        String basePath = existDirectory(pathSystem.DIRECTORY_PROJECTS, uuid, baseProjectName);

        if(!saveFile(eaf, basePath, baseProjectName)){
            return pathSystem.NOT_UPLOAD_EAF_FILE;
        }

        if(!saveFile(eaf, basePath, baseProjectName + "_ORIGINAL")) {
            return pathSystem.NOT_UPLOAD_EAF_FILE;
        }

        boolean rs1 = dbReports.deactivateAllReportes(id_project);
        boolean rs2 = dbAnnotations.deleteGlossingRecords(id_project);
        InitValidateElanXmlDigester(eaf, uuid, basePath, baseProjectName, id_project);

        return pathSystem.SUCCESS_UPLOAD;
    }

    public Number updateMultimedia(MultipartFile multimedia, String projectName, String uuid, int id) throws IOException, SQLException {
        String baseProjectName = projectName.replace(" ", "_");
        String basePath = existDirectory(pathSystem.DIRECTORY_PROJECTS, uuid, baseProjectName);

        if(!saveFile(multimedia, basePath, baseProjectName)){
            return pathSystem.NOT_UPLOAD_MULTIMEDIA_FILE;
        }

        return pathSystem.SUCCESS_UPLOAD;
    }
    public Number updateImages(MultipartFile[] images, String projectName, String uuid, int id) throws IOException, SQLException {
        String baseProjectName = projectName.replace(" ", "_");
        String basePath = existDirectory(pathSystem.DIRECTORY_PROJECTS, uuid, baseProjectName);
        int index = 0;
        String ImageFolder = basePath+"Images/";
        if(Files.exists(Path.of(ImageFolder))){
            String[] pathnames;
            File f = new File(ImageFolder);
            pathnames = f.list();
            Arrays.sort(pathnames);
            for (String pathname : pathnames) {
                String x = FilenameUtils.getBaseName(pathname);
                index = Integer.parseInt(x.split("image")[1]);
            }
        }else{
            File newSubDirectory = new File(ImageFolder);
            newSubDirectory.mkdirs();
        }
        for(MultipartFile file : images) {
            if(!saveFile(file, ImageFolder, baseProjectName+"_image"+(index+=1))){
                return pathSystem.NOT_UPLOAD_AUTORIZATION_FILE;
            }
        }

        return pathSystem.SUCCESS_UPLOAD;
    }

    /**
     * Inicializacion de los path's para el thread y la queue
     * @param eaf MultipartFile, Archivo de anotaciones
     * @param uuid String, Identificador de usuario
     * @param projectName String, Nombre del proyecto
     */
    public void InitValidateElanXmlDigester(MultipartFile eaf, String uuid, String basePath, String projectName, int projectID){
        System.out.println("InitElanXmlDigester.....");
        String extEaf = FilenameUtils.getExtension(eaf.getOriginalFilename());
        String pathEaf = basePath+projectName+"."+extEaf;
        threadValidateElanXmlDigester.add(pathEaf, uuid, projectID);
        threadValidateElanXmlDigester.activate();
    }
}
