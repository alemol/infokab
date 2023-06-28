package mx.geoint.Controllers.FFmpeg;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FFprobeTest {
    private FFprobe ffprobe;
    private String path ="src/main/resources/";
    private String source ="eligio_uikab_mena.wav";
    @Test
    void getMetadata() {
        try{
            ffprobe = new FFprobe();
            String response = ffprobe.getMetadata(path+source);
            System.out.println(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}