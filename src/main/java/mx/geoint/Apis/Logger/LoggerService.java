package mx.geoint.Apis.Logger;

import mx.geoint.pathSystem;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

@Service
public class LoggerService {

    public LoggerService(){ }

    public ArrayList<String> getLoggerFiles() {
        File dir = new File(pathSystem.DIRECTORY_LOG);
        File[] files = dir.listFiles();
        ArrayList<String> listName = new ArrayList<>();

        for (File file : files) {
            listName.add(file.getName());
        }
        return listName;
    }

    public String readLoggerFile(String fileName) throws IOException {
        //File file = new File(pathSystem.DIRECTORY_LOG+fileName);
        //ArrayList<String> listName = new ArrayList<>();
        String content = Files.readString(Path.of(pathSystem.DIRECTORY_LOG + fileName));
        return content;
    }
}
