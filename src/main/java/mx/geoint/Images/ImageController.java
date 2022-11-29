package mx.geoint.Images;


import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.File;

import static mx.geoint.pathSystem.DIRECTORY_PROJECTS;

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
     *
     * @return ArraList<String> Lista de los nombre de los archivos
     */
    @RequestMapping(path = "/readpath", method = RequestMethod.POST, consumes = "application/json")
    //@PostMapping(value = "/updatePerson", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public GalleryModel Filelist(@RequestBody routeModel route) {
        String dir = route.getRoute() + "/Images/";

        int lastIndex = 0;
        String[] pathnames;

        File f = new File(dir);

        pathnames = f.list();

        for (String pathname : pathnames) {
            String x = FilenameUtils.getBaseName(pathname);
            lastIndex = Integer.parseInt(x.split("image")[1]);
        }
        GalleryModel galleryModel = new GalleryModel();
        galleryModel.setimageList(pathnames);
        galleryModel.setLastIndex(lastIndex);
        System.out.println(pathnames.length);

        return galleryModel;
    }

}
