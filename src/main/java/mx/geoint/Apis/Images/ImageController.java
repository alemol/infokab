package mx.geoint.Apis.Images;


import mx.geoint.Model.Image.routeModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static mx.geoint.pathSystem.DIRECTORY_PROJECTS;

@CrossOrigin(origins = {"http://www.taantsil.com","http://www.taantsil.com.mx/","http://www.infokaab.com/","http://www.infokaab.com.mx/","http://taantsil.com","http://taantsil.com.mx/","http://infokaab.com/","http://infokaab.com.mx/","http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009","http://10.2.102.182","http://10.2.102.189:3009"})
@RestController
@RequestMapping(path = "api/images")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * Api para obtener una lista de imagenes contenidos en un fichero
     *
     * @return ArraList<String> Lista de los nombre de los archivos
     */
    @RequestMapping(path = "/deleteImage", method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public boolean Filelist(@RequestBody routeModel route) {
        boolean isFound = route.getRoute().split("Project/")[1].indexOf("Images") != -1 ? true: false;
        if(isFound){
            String dirFull = DIRECTORY_PROJECTS+ route.getRoute().split("Project/")[1].replaceAll("Images", "ImagesFull").replaceAll("image", "imageFull");

            Boolean deletedFull = false;
            if(Files.exists(Path.of(dirFull))) {
                try {
                    File f = new File(dirFull); //file to be delete
                    deletedFull = f.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        String dir = DIRECTORY_PROJECTS+ route.getRoute().split("Project/")[1];
        Boolean deleted = false;
        if(Files.exists(Path.of(dir))) {
            try {
                File f = new File(dir); //file to be delete
                deleted = f.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return deleted;
    }

}
