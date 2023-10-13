package mx.geoint.Apis.UploadFiles;

import mx.geoint.Model.Project.ProjectPostgresLocationCoincidence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

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
    public Number uploadFile(MultipartFile eaf, MultipartFile multimedia, MultipartFile autorizacion , MultipartFile[] images, String uuid, String projectName, String date, String hablantes, String ubicacion, String radio, String circleBounds, String localidad_nombre, String localidad_cvegeo, String mimeType) throws IOException, SQLException {
        return uploadFiles.uploadFile(eaf, multimedia, autorizacion, images, uuid, projectName, date, hablantes, ubicacion, radio, circleBounds, localidad_nombre, localidad_cvegeo, mimeType);
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

    public URL validateLink(String link) throws IOException {
        URL url = new URL(link);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.getHeaderFields();
        //System.out.println("Original URL: "+ httpURLConnection.getURL());
        httpURLConnection.disconnect();
        return httpURLConnection.getURL();
    }

}
