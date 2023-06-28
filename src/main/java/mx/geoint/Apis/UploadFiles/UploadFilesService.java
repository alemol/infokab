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
    public Number uploadFile(MultipartFile eaf, MultipartFile multimedia, MultipartFile autorizacion , MultipartFile[] images, String uuid, String projectName, String date, String hablantes, String ubicacion, String radio, String circleBounds, String localidad_nombre, String localidad_cvegeo) throws IOException, SQLException {
        return uploadFiles.uploadFile(eaf, multimedia, autorizacion, images, uuid, projectName, date, hablantes, ubicacion, radio, circleBounds, localidad_nombre, localidad_cvegeo);
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
        URL url = new URL("https://www.google.com.mx/maps/@20.0334236,-88.5424351,3a,75y,338.19h,90t/data=!3m7!1e1!3m5!1sdls_yCl_kmsafEYgVf9Yyg!2e0!6shttps:%2F%2Fstreetviewpixels-pa.googleapis.com%2Fv1%2Fthumbnail%3Fpanoid%3Ddls_yCl_kmsafEYgVf9Yyg%26cb_client%3Dsearch.revgeo_and_fetch.gps%26w%3D96%26h%3D64%26yaw%3D338.18787%26pitch%3D0%26thumbfov%3D100!7i13312!8i6656?shorturl=1");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.getHeaderFields();
        //System.out.println("Original URL: "+ httpURLConnection.getURL());
        httpURLConnection.disconnect();
        return httpURLConnection.getURL();
    }

}
