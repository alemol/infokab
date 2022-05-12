package mx.geoint;

import mx.geoint.FFmpeg.FFmpeg;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@SpringBootApplication
@RestController
public class Infokab {
    public static void main(String[] args) {
        SpringApplication.run(Infokab.class, args);
    }

    @RequestMapping(value = "/cutAudio/{source}/{start}/{duration}/{output}", method = GET)
    @ResponseBody
    public void cutAudio(@PathVariable String source, @PathVariable double start, @PathVariable double duration, @PathVariable String output) {
        FFmpeg multimedia = new FFmpeg("/home/ulises/Descargas/archivos_kellogs/");
        multimedia.cortador(source,start,duration,output);
    }
}
