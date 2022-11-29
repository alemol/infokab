package mx.geoint.Images;


import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.File;

//@CrossOrigin(origins = {"http://infokaab.com/","http://infokaab.com.mx/","http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009","http://10.2.102.182"})
@RestController
@RequestMapping(path = "api/images")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * Api para obtener una lista de imagenes contenidos en un fichero
     * @return ArraList<String> Lista de los nombre de los archivos
     */
    @RequestMapping(path = "/readpath", method = RequestMethod.POST,  consumes = "application/json")
    //@PostMapping(value = "/updatePerson", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public boolean Filelist(@RequestBody routeModel route) {
        //String route = "/home/centrogeo/JavaApps/infokab-backend/Files/Project/08a84f2a-ad52-4c12-b9a3-f0d38d5cd81d";
        System.out.println(route);
        int lastIndex = 0;
        String[] pathnames;

        File f = new File(route.getRoute());

        // Populates the array with names of files and directories
        pathnames = f.list();

        // For each pathname in the pathnames array
        for (String pathname : pathnames) {
            // Print the names of files and directories
            String x = FilenameUtils.getBaseName(pathname);
            lastIndex = Integer.parseInt(x.split("image")[1]);
        }
        System.out.println(lastIndex);
        System.out.println(pathnames.length);

        return false;
    }

}
