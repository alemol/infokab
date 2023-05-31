package mx.geoint.Controllers.Images;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ImagesTest {
    private String path = "src/main/resources/";
    private String source = "input.jpg";
    private String output = "output.jpg";
    private Images img;
    @Test
    void resizer() throws IOException, InterruptedException {
        img = new Images(path);
        img.resizer(path+source,path+output);
    }
}