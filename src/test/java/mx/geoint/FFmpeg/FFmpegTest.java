package mx.geoint.FFmpeg;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

class FFmpegTest {

    private FFmpeg ffmpeg;
    private String output = "a5_Transcripción_ts1_ts5_34240_39000_eligio_uikab_mena.wav";
    private String path ="src/main/resources/";
    private String source ="eligio_uikab_mena.wav";
    @Test
    void testCortador() {
        ffmpeg = new FFmpeg(path);
        ffmpeg.cortador(source,34.240,4.70,output);
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