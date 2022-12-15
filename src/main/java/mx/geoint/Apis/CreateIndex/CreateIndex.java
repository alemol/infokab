package mx.geoint.Apis.CreateIndex;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CreateIndex {

    public CreateIndex(){}

    /*
    * Regresa una confirmación de indice creado
    * @param name Nombre del indice a crear en el path DIRECTORY_INDEX_GENERAL
    * @return confirmación del finalización del proceso
    **/
    public String createdIndex(String text) throws IOException {
        Gson gson = new Gson();
        CreateIndexModel createIndexModel = gson.fromJson(text, CreateIndexModel.class);

        //Lucene lucene = new Lucene(pathSystem.DIRECTORY_INDEX_GENERAL+createIndexModel.name+"/");
        //lucene.initConfig(true);
        //lucene.createIndex(pathSystem.DIRECTORY_FILES_JSON);

        System.out.println("Create Index");
        return "ok";
    }
}
