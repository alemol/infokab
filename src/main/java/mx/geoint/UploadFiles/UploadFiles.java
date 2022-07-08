package mx.geoint.UploadFiles;

import mx.geoint.pathSystem;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class UploadFiles {
    public UploadFiles(){

    }

    public boolean uploadFile(MultipartFile eaf, MultipartFile multimedia, String uuid) throws IOException {
        if(!saveFile(eaf, uuid)){
            return false;
        }

        if(!saveFile(multimedia, uuid)){
            return false;
        }

        return true;
    }

    public boolean saveFile(MultipartFile file, String uuid) throws IOException {
        String name = file.getOriginalFilename();
        String contentType = file.getContentType();
        long size = file.getSize();
        System.out.println("SIZE :" + size);

        String currentDirectory = existDirectory(pathSystem.DIRECTORY_MULTIMEDIA, uuid);

        byte[] bytes = file.getBytes();
        Path path = Paths.get(currentDirectory + name);
        Files.write(path, bytes);

        System.out.println("PATH :" + name);
        System.out.println("contentType :" + contentType);

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
