package mx.geoint.Controllers.Images;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class Images {
    String path;
    String s;

    /* Constructor */
    public Images(String path) {
        this.path = path;
        this.s = null;
    }

    /*
     * Método que corta el audio o video
     * Recibe como parámetro:
     * - source: que es el nombre del archivo fuente.
     * - start: que es el segundo en el cual inicia el fragmento que deseamos recortar (TIME_VALUE1/1000).
     * - duration: es el tiempo que va a tardar el fragmento de video (se obtiene restando (TIME_VALUE2 - TIME_VALUE1)/1000).
     * - output: es el nombre con el cual se va a guardar el nuevo fragmento.
     * */
    public boolean resizer(String source) throws IOException {
        boolean creado = false;
        String NewPath = path.replace("ImagesFull","Images");
        String output = source.replace("imageFull","image");
        existDirectory(NewPath);

        // creamos el cuerpo del comando que se va a ejecutar.

        Process p = Runtime.getRuntime().exec(new String[]{
                "convert",
                path+source,
                "-resize",
                "320x240",
                NewPath+output
        });

        // Atrapamos la respuesta en caso de que sea exitoso.
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(p.getInputStream()));
        // Atrapamos la respuesta en caso de que genere errores.
        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(p.getErrorStream()));
        // Recorremos la salida e imprimimos los resultados
        System.out.println(new String[]{
                "convert",
                path+source,
                "-quality", "1",
                "-resize",
                "320x240",
                NewPath+output
        });
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }
        // Recorremos la salida e imprimimos los errores.
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }

        int exitCode = 0;
        try {
            exitCode = p.waitFor();
            if(exitCode==0){
                creado = true;
            }else {
                creado=false;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        return creado;
    }

    private String existDirectory(String pathDirectory){
        String currentDirectory = pathDirectory;

        if(!Files.exists(Path.of(pathDirectory))){
            File newSubDirectory = new File(pathDirectory);
            newSubDirectory.mkdirs();
        }

        return currentDirectory;
    }
}
