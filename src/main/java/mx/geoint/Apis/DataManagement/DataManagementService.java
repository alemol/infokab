package mx.geoint.Apis.DataManagement;

import mx.geoint.Apis.UploadFiles.UploadFiles;
import mx.geoint.Controllers.FFmpeg.FFprobe;
import mx.geoint.Database.DBProjects;
import mx.geoint.Database.DBReports;
import mx.geoint.Model.Project.ProjectPostgresRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

@Service
public class DataManagementService {

    public static DBProjects dbProjects;

    public DataManagementService(){
        this.dbProjects = new DBProjects();
    }

    public boolean setStorage(String id) throws SQLException, IOException, InterruptedException {
        System.out.println("id service : "+ id);
        ProjectPostgresRegister projectPostgresRegister = dbProjects.getProjectById(id);
        String ruta_trabajo = projectPostgresRegister.getRuta_trabajo();
        String nombre_de_proyecto = projectPostgresRegister.getNombre_proyecto();
        String mime_type = projectPostgresRegister.getMime_type();
        String ext = "";
        if(mime_type.toLowerCase().equals("audio/wav")){
            ext = ".wav";
        }else{
            ext = ".mp4";
        }
        String fullMultimedia = ruta_trabajo +nombre_de_proyecto + ext;
        System.out.println("DATA PATH: " + fullMultimedia);

        FFprobe ffprobe = new FFprobe();
        String metadata = ffprobe.getMetadata(fullMultimedia);
        System.out.println("DATA PATH: " + metadata);
        boolean updatedMetadata = dbProjects.updateMetadataOfProject(metadata, id);
        return true;
    }

}
