package mx.geoint.VideoCutter;

import mx.geoint.Controllers.FFmpeg.FFmpeg;
import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Controllers.VideoCutter.VideoCutter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class VideoCutterTest {
    private Logger logger = new Logger();
    private VideoCutter videoCutter;
    private String output = "a5_Transcripción_ts1_ts5_34240_39000_eligio_uikab_mena.wav";
    private String path ="src/main/resources/";
    private String source ="eligio_uikab_mena.wav";
    @Test
    void testCortador() {
        try{
            videoCutter = new VideoCutter(path);
            videoCutter.cortador(source,34.240,4.70,output);
        } catch (IOException e) {
            logger.appendToFile(e);
            throw new RuntimeException(e);
        }
    }

    @Test
    void testIf_exist() {
        System.out.println("exist: "+ Files.exists(Path.of(path + output)));
    }

    @Test
    void testEliminar() {
        File myObj = new File(path + output);
        myObj.delete();
    }
}