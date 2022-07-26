package mx.geoint.UploadFiles;

import mx.geoint.CreateIndex.CreateIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class UploadFilesService {

    @Autowired
    UploadFiles uploadFiles;

    public boolean uploadFile(MultipartFile eaf, MultipartFile multimedia, String uuid, String projectName) throws IOException {
        return uploadFiles.uploadFile(eaf, multimedia, uuid, projectName);
    }
}
