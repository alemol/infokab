package mx.geoint.FFmpeg;

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
    public boolean cortador(String source, double start, double duration, String output) {
        boolean creado = false;
        // Metemos la función en un try catch para manejar los errores sin interrumpir la ejecución.
        try {
            // Comprobamos si existe un archivo cortado con el mismo nomnbre, si existe mandamos a llamar a la función que lo elimina.
            if (if_exist(output)) {
                eliminar(output);
            }
            // creamos el cuerpo del comando que se va a ejecutar.
            Process p = Runtime.getRuntime().exec(new String[]{
                    "ffmpeg",
                    "-i",
                    path + source,
                    "-ss",
                    String.valueOf(start),
                    "-t",
                    String.valueOf(duration),
                    "-c",
                    "copy",
                    path + output
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
                //System.out.println(s);
            }
            creado = true;
            //System.exit(0);
        } catch (IOException e) {
            creado = false;
            System.out.println("Ocurrió un error: ");
            e.printStackTrace();
            System.exit(-1);
        }
        return creado;
    }

    /* Método para comprobar si existe un archivo con el mismo nombre */
    public boolean if_exist(String output) {
        return Files.exists(Path.of(path + output));
    }

    /* Método que elimina el archivo */
    public void eliminar(String output) {
        File myObj = new File(path + output);
        myObj.delete();
    }
}
