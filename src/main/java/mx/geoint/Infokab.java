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
}


