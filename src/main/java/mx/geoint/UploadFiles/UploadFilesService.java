package mx.geoint.UploadFiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

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
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<Number> uploadFile(MultipartFile eaf, MultipartFile multimedia, String uuid, String projectName) throws IOException, InterruptedException {
        System.gc();
        Runtime.getRuntime().gc();
        Thread.sleep(2000);
        System.out.println("Execute method with configured executor - " + Thread.currentThread().getName());
        //return uploadFiles.uploadFile(eaf, multimedia, uuid, projectName);
        return CompletableFuture.completedFuture(uploadFiles.uploadFile(eaf, multimedia, uuid, projectName));
    }
}
