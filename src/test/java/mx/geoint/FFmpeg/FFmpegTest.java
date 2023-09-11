package mx.geoint.FFmpeg;

import mx.geoint.Controllers.FFmpeg.FFmpeg;
import mx.geoint.Controllers.Logger.Logger;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class FFmpegTest {
    private Logger logger = new Logger();
    private FFmpeg ffmpeg;
    private String output = "a5_Transcripción_ts1_ts5_34240_39000_eligio_uikab_mena.wav";
    private String path ="src/main/resources/";
    private String source ="eligio_uikab_mena.wav";
    @Test
    void testCortador() {
        try{
            ffmpeg = new FFmpeg(path);
            ffmpeg.cortador(source,34.240,4.70,output);
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


    @Test
    String formatedMillis(int durationInMillis) {
        long millis = durationInMillis % 1000;
        long second = (durationInMillis / 1000) % 60;
        long minute = (durationInMillis / (1000 * 60)) % 60;
        long hour = (durationInMillis / (1000 * 60 * 60)) % 24;

        String time = String.format("%02d:%02d:%02d.%d", hour, minute, second, millis);
        return time;
    }

    @Test
    void millisToTime() {
        int start = 2071838;
        int end = 2081022;
        int diff = 2081022 - 2071838;
        System.out.println(formatedMillis(start));
        System.out.println(formatedMillis(diff));
    }

}