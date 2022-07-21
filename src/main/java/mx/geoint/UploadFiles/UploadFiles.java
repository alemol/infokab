package mx.geoint.UploadFiles;

import mx.geoint.ElanXmlDigester.ThreadElanXmlDigester;
import mx.geoint.pathSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
        if(!saveFile(eaf, uuid, pathSystem.DIRECTORY_ANNOTATION)){
            return false;
        }

        if(!saveFile(multimedia, uuid, pathSystem.DIRECTORY_MULTIMEDIA)){
            return false;
        }

        InitElanXmlDigester(eaf, uuid);
        return true;
    }

    public void InitElanXmlDigester(MultipartFile eaf, String uuid){
        String name = eaf.getOriginalFilename();
        String pathEaf = pathSystem.DIRECTORY_ANNOTATION+uuid+"/"+name;
        threadElanXmlDigester.add(pathEaf, uuid);
        threadElanXmlDigester.activate();
    }

    public boolean saveFile(MultipartFile file, String uuid, String directory) throws IOException {
        String name = file.getOriginalFilename();
        String contentType = file.getContentType();
        long size = file.getSize();

        String currentDirectory = existDirectory(directory, uuid);

        Path path = Paths.get(currentDirectory + name);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("PATH :" + name);
        System.out.println("contentType :" + contentType);
        System.out.println("Size :" + size);

        return true;
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
