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

    public boolean uploadFile(MultipartFile eaf, MultipartFile multimedia, String uuid) throws IOException {
        if(!saveFile(eaf, uuid, pathSystem.DIRECTORY_PROJECTS)){
            return false;
        }

        if(!saveFile(multimedia, uuid, pathSystem.DIRECTORY_PROJECTS)){
            return false;
        }

        InitElanXmlDigester(eaf, multimedia, uuid, pathSystem.DIRECTORY_PROJECTS);
        return true;
    }

    public void InitElanXmlDigester(MultipartFile eaf, MultipartFile multimedia, String uuid, String directory){
        String nameEaf = eaf.getOriginalFilename();
        String baseNameEaf = FilenameUtils.getBaseName(nameEaf);

        String nameMultimedia = multimedia.getOriginalFilename();
        String baseNameMultimedia = FilenameUtils.getBaseName(nameMultimedia);

        String pathEaf = directory+ uuid+"/"+baseNameEaf+"/"+nameEaf;
        String pathMultimedia = directory+ uuid+"/"+baseNameMultimedia+"/"+nameMultimedia;
        threadElanXmlDigester.add(pathEaf, pathMultimedia, uuid);
        threadElanXmlDigester.activate();
    }

    public boolean saveFile(MultipartFile file, String uuid, String directory) throws IOException {
        Date startDate = new Date();
        String name = file.getOriginalFilename();
        String baseName = FilenameUtils.getBaseName(name);

        String contentType = file.getContentType();
        long size = file.getSize();
        String currentDirectory = existDirectory(directory, uuid, baseName);
        Path path = Paths.get(currentDirectory + name);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        Date endDate = new Date();
        long difference_In_Time = endDate.getTime() - startDate.getTime();
        long difference_In_Seconds = (difference_In_Time / (1000)) % 60;
        long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
        System.out.println("UUID: "+ uuid +" PATH: " + name + " SIZE: "+ size + " SAVE_TIME: "+ difference_In_Seconds +"s " + difference_In_Minutes+"m");
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
