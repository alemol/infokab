package mx.geoint.UploadFiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UploadFilesService {

    @Autowired
    UploadFiles uploadFiles;

    /**
     *
     * @param eaf MultipartFile, Archivo de anotaciones
     * @param multimedia MultipartFile, Archivo de multimedia audio o video
     * @param uuid String, Identificador de usuario
     * @param projectName String, Nombre del proyecto
     * @return boolean, respuesta del servicio
     * @throws IOException
     */
    public Number uploadFile(MultipartFile eaf, MultipartFile multimedia, MultipartFile autorizacion, String uuid, String projectName) throws IOException {
        return uploadFiles.uploadFile(eaf, multimedia, autorizacion, uuid, projectName);
    }
}
