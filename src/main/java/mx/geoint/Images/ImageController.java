package mx.geoint.Images;


import mx.geoint.User.User;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.sql.SQLException;
import java.util.*;

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
    public boolean Filelist(@RequestParam String route) {
        System.out.println(route);
        int lastIndex = 0;
        String[] pathnames;

        File f = new File(route);

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
