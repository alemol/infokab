package mx.geoint.UploadFiles;

import mx.geoint.ElanXmlDigester.ThreadElanXmlDigester;
import mx.geoint.pathSystem;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Component
public class UploadFiles {
    //@Autowired
    //ThreadElanXmlDigester threadElanXmlDigester;
    private final ThreadElanXmlDigester threadElanXmlDigester;
    public UploadFiles(){
        threadElanXmlDigester = new ThreadElanXmlDigester();
        threadElanXmlDigester.start();
    }

    public boolean uploadFile(MultipartFile eaf, MultipartFile multimedia, String uuid, String projectName) throws IOException {
        String baseProjectName = projectName.replace(" ", "_");
        String basePath = existDirectory(pathSystem.DIRECTORY_PROJECTS, uuid, baseProjectName);
        if(!saveFile(eaf, basePath, baseProjectName)){
            return false;
        }

        if(!saveFile(multimedia, basePath, baseProjectName)){
            return false;
        }

        InitElanXmlDigester(eaf, multimedia, uuid, basePath, baseProjectName);
        return true;
    }

    public void InitElanXmlDigester(MultipartFile eaf, MultipartFile multimedia, String uuid, String basePath, String projectName){
        String extEaf = FilenameUtils.getExtension(eaf.getOriginalFilename());
        String extMultimedia = FilenameUtils.getExtension(multimedia.getOriginalFilename());

        String pathEaf = basePath+projectName+"."+extEaf;
        String pathMultimedia = basePath+projectName+"."+extMultimedia;
        threadElanXmlDigester.add(pathEaf, pathMultimedia, uuid);
        threadElanXmlDigester.activate();
    }

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

    private String existDirectory(String pathDirectory, String uuid, String baseName){
        String currentDirectory = pathDirectory + uuid + "/"+baseName+"/";

        if(!Files.exists(Path.of(currentDirectory))){
            File newSubDirectory = new File(currentDirectory);
            newSubDirectory.mkdirs();
        }

        return currentDirectory;
    }
}
