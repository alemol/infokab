package mx.geoint.UploadFiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

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
    public Number uploadFile(MultipartFile eaf, MultipartFile multimedia, MultipartFile autorizacion , MultipartFile[] images, String uuid, String projectName, String date, String hablantes, String ubicacion, String radio, String circleBounds) throws IOException, SQLException {
        return uploadFiles.uploadFile(eaf, multimedia, autorizacion, images, uuid, projectName, date, hablantes, ubicacion, radio, circleBounds);
    }

    public Number updateEaf(MultipartFile eaf, String projectName, String uuid, int id) throws IOException, SQLException {
        return uploadFiles.updateEaf(eaf, projectName, uuid, id);
    }

    public Number updateMultimedia(MultipartFile multimedia, String projectName, String uuid, int id) throws IOException, SQLException {
        return uploadFiles.updateMultimedia(multimedia, projectName, uuid, id);
    }

    public Number updateImages(MultipartFile[] images, String projectName, String uuid, int id) throws IOException, SQLException {
        return uploadFiles.updateImages(images, projectName, uuid, id);
    }

}
