package mx.geoint.Controllers.FFmpeg;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class FFmpeg {
    String path;
    String s;

    /* Constructor */
    public FFmpeg(String path) {
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
    public boolean cortador(String source, double start, double duration, String output) throws IOException {
        boolean creado = false;
        System.out.println("start: "+ start);
        System.out.println("end: "+ duration);
        String basePath = FilenameUtils.getPath(source)+"multimedia/";
        existDirectory(basePath);
        // Comprobamos si existe un archivo cortado con el mismo nomnbre, si existe mandamos a llamar a la función que lo elimina.
        //if (if_exist(basePath+output)) {
        //    return true;
        //}


        // creamos el cuerpo del comando que se va a ejecutar.
        Process p = Runtime.getRuntime().exec(new String[]{
                "ffmpeg",
                "-i",
                source,
                "-ss",
                formatedMillis(start),
                "-t",
                formatedMillis(duration),
                "-c",
                "copy",
                "-y",
                basePath+output
        });
        // Atrapamos la respuesta en caso de que sea exitoso.
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(p.getInputStream()));
        // Atrapamos la respuesta en caso de que genere errores.
        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(p.getErrorStream()));
        // Recorremos la salida e imprimimos los resultados
        while ((s = stdInput.readLine()) != null) {
            //System.out.println(s);
        }
        // Recorremos la salida e imprimimos los errores.
        while ((s = stdError.readLine()) != null) {
            if(s.indexOf("size=")==0) {
                //System.out.println(s.indexOf("size=")+" "+s);
                creado = true;
            }
            if(s.indexOf("No such file or directory")!=-1){
                //System.out.println(s+" "+s.indexOf("No such file or directory"));
                creado = false;
            }

        }
        //System.exit(0);

        return creado;
    }

    /* Método para comprobar si existe un archivo con el mismo nombre */
    public boolean if_exist(String output) {
        return Files.exists(Path.of(output));
    }

    /* Método que elimina el archivo */
    public void eliminar(String output) {
        File myObj = new File(output);
        myObj.delete();
    }

    private String existDirectory(String pathDirectory){
        String currentDirectory = pathDirectory;

        if(!Files.exists(Path.of(pathDirectory))){
            File newSubDirectory = new File(pathDirectory);
            newSubDirectory.mkdirs();
        }

        return currentDirectory;
    }

    private String formatedMillis(double durationInMillis) {
        long millis = (long) (durationInMillis % 1000);
        long second = (long) ((durationInMillis / 1000) % 60);
        long minute = (long) ((durationInMillis / (1000 * 60)) % 60);
        long hour = (long) ((durationInMillis / (1000 * 60 * 60)) % 24);

        String time = String.format("%02d:%02d:%02d.%d", hour, minute, second, millis);
        System.out.println("time: "+time);
        return time;
    }
}
