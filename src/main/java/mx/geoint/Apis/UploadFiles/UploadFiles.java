package mx.geoint.Apis.UploadFiles;

import mx.geoint.Controllers.ElanXmlDigester.ThreadElanXmlDigester;
import mx.geoint.Controllers.ElanXmlDigester.ThreadValidateElanXmlDigester;
import mx.geoint.Controllers.Images.Images;
import mx.geoint.Database.DBAnnotations;
import mx.geoint.Database.DBProjects;
import mx.geoint.Database.DBReports;
import mx.geoint.Database.DBUsers;
import mx.geoint.Model.Project.ProjectPostgresRegister;
import mx.geoint.Model.Project.ProjectPostgresLocationCoincidence;
import mx.geoint.pathSystem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public Number uploadFile(MultipartFile eaf, MultipartFile multimedia, MultipartFile autorizacion, MultipartFile[] images, String uuid, String projectName, String date, String hablantes, String ubicacion, String radio, String circleBounds, String localidad_nombre, String localidad_cvegeo, String mimeType) throws IOException, SQLException {
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
            String ImageFolder = basePath+"ImagesFull/";
            String VideoFolder = basePath+"Video/";
            File imageDirectory = new File(ImageFolder);
            File videoDirectory = new File(VideoFolder);
            imageDirectory.mkdirs();
            videoDirectory.mkdirs();
            for(MultipartFile file : images) {
                if(file.getContentType().equals("video/mp4")){
                    if(!saveFile(file, VideoFolder, baseProjectName+"_video"+(index++))){
                        return pathSystem.NOT_UPLOAD_AUTORIZATION_FILE;
                    }
                }else{
                    if(!saveFile(file, ImageFolder, baseProjectName+"_imageFull"+(index++))){
                        return pathSystem.NOT_UPLOAD_AUTORIZATION_FILE;
                    }
                }

            }

        }

        int id_project = dbProjects.createProject(uuid, basePath, baseProjectName, date, hablantes, ubicacion, radio, circleBounds, localidad_nombre, localidad_cvegeo, mimeType); //inserta un registro del proyecto en la base de datos

        System.out.println("ID de proyecto generado: "+id_project);
        if(id_project > 0){
            System.out.println("Proyecto guardado en base de datos");
            InitElanXmlDigester(eaf.getOriginalFilename(), multimedia.getOriginalFilename(), uuid, basePath, baseProjectName, id_project);
            //InitValidateElanXmlDigester(eaf, uuid, basePath, baseProjectName, (Integer) id_project);
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
    public void InitElanXmlDigester(String eaf, String multimedia, String uuid, String basePath, String projectName, int projectID) throws SQLException {
        System.out.println("InitElanXmlDigester.....");
        String extEaf = FilenameUtils.getExtension(eaf);
        String extMultimedia = FilenameUtils.getExtension(multimedia);

        String pathEaf = basePath+projectName+"."+extEaf;
        String pathMultimedia = basePath+projectName+"."+extMultimedia;
        dbProjects.updateProcess(projectID, true);
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

    public Number updateEaf(MultipartFile eaf, String projectName, String uuid, int id_project) throws IOException, SQLException {
        ProjectPostgresRegister projectPostgresRegister = this.dbProjects.getProjectById(String.valueOf(id_project));

        String baseProjectName = projectName.replace(" ", "_");
        String basePath = existDirectory(pathSystem.DIRECTORY_PROJECTS, uuid, baseProjectName);

        if(!saveFile(eaf, basePath, baseProjectName)){
            return pathSystem.NOT_UPLOAD_EAF_FILE;
        }

        if(!saveFile(eaf, basePath, baseProjectName + "_ORIGINAL")) {
            return pathSystem.NOT_UPLOAD_EAF_FILE;
        }

        File dir_maya = new File(basePath+"/maya/");
        FileUtils.deleteDirectory(dir_maya.getCanonicalFile());

        File dir_español = new File(basePath+"/español/");
        FileUtils.deleteDirectory(dir_español.getCanonicalFile());

        File dir_multimedia = new File(basePath+"/multimedia/");
        FileUtils.deleteDirectory(dir_multimedia.getCanonicalFile());

        //String multimedia = FilenameUtils.getBaseName(baseProjectName)+".wav";
        String multimedia = "";

        if(projectPostgresRegister.getMime_type().equals("audio/wav")){
            multimedia = FilenameUtils.getBaseName(baseProjectName)+".wav";
        }else{
            multimedia = FilenameUtils.getBaseName(baseProjectName)+".mp4";
        }
        boolean rs1 = dbReports.deactivateAllReportes(id_project);
        boolean rs2 = dbAnnotations.deleteGlossingRecords(id_project);
        //InitValidateElanXmlDigester(eaf, uuid, basePath, baseProjectName, id_project);
        InitElanXmlDigester(eaf.getOriginalFilename(), multimedia, uuid, basePath, baseProjectName, id_project);

        return pathSystem.SUCCESS_UPLOAD;
    }

    public Number updateMultimedia(MultipartFile multimedia, String projectName, String uuid, int id_project) throws IOException, SQLException {
        String baseProjectName = projectName.replace(" ", "_");
        String basePath = existDirectory(pathSystem.DIRECTORY_PROJECTS, uuid, baseProjectName);

        if(!saveFile(multimedia, basePath, baseProjectName)){
            return pathSystem.NOT_UPLOAD_MULTIMEDIA_FILE;
        }

        File dir_maya = new File(basePath+"/maya/");
        FileUtils.deleteDirectory(dir_maya.getCanonicalFile());

        File dir_español = new File(basePath+"/español/");
        FileUtils.deleteDirectory(dir_español.getCanonicalFile());

        File dir_multimedia = new File(basePath+"/multimedia/");
        FileUtils.deleteDirectory(dir_multimedia.getCanonicalFile());

        String eaf = FilenameUtils.getBaseName(baseProjectName)+".eaf";
        InitElanXmlDigester(eaf, multimedia.getOriginalFilename(), uuid, basePath, baseProjectName, id_project);

        return pathSystem.SUCCESS_UPLOAD;
    }
    public Number updateImages(MultipartFile[] images, String projectName, String uuid, int id) throws IOException, SQLException {
        String baseProjectName = projectName.replace(" ", "_");
        String basePath = existDirectory(pathSystem.DIRECTORY_PROJECTS, uuid, baseProjectName);
        int index = 0;
        int imageIndex = 0;
        int videoIndex = 0;
        String ImageFolder = basePath+"ImagesFull/";
        String ImagesReducesFolder = basePath+"Images/";
        String VideoFolder = basePath+"Video/";
        if(!Files.exists(Path.of(ImageFolder))){
            File newSubDirectory = new File(ImageFolder);
            newSubDirectory.mkdirs();
        }

        if(Files.exists(Path.of(ImagesReducesFolder))) {
            String[] pathnames;
            File f = new File(ImagesReducesFolder);
            pathnames = f.list();
            Arrays.sort(pathnames);
            for (String pathname : pathnames) {
                String x = FilenameUtils.getBaseName(pathname);
                imageIndex = Integer.parseInt(x.split("image")[1].replace("Full", ""));
            }
        }else{
            File newSubDirectory = new File(ImagesReducesFolder);
            newSubDirectory.mkdirs();
        }

        if(Files.exists(Path.of(VideoFolder))){
            String[] pathnames;
            File f = new File(VideoFolder);
            pathnames = f.list();
            Arrays.sort(pathnames);
            for (String pathname : pathnames) {
                String x = FilenameUtils.getBaseName(pathname);
                videoIndex = Integer.parseInt(x.split("video")[1]);
            }
        }else{
            File newSubDirectory = new File(VideoFolder);
            newSubDirectory.mkdirs();
        }

        index = Math.max(imageIndex, videoIndex);

        for(MultipartFile file : images) {
            if(file.getContentType().equals("video/mp4")){
                if(!saveFile(file, VideoFolder, baseProjectName+"_video"+(index+=1))){
                    return pathSystem.NOT_UPLOAD_AUTORIZATION_FILE;
                }
            }else{
                if(!saveFile(file, ImageFolder, baseProjectName+"_imageFull"+(index+=1))){
                    return pathSystem.NOT_UPLOAD_AUTORIZATION_FILE;
                }
            }

        }

        if(Files.exists(Path.of(ImageFolder))) {
            Images img = new Images(ImageFolder);
            String[] pathnames;
            File f = new File(ImageFolder);
            pathnames = f.list();
            for (int i = 0; i < pathnames.length; i++) {
                System.out.println(pathnames[i]+" reducida: "+ img.resizer(pathnames[i]));
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
