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
        long mb = 1024*1024;
        System.out.println("\t Used Memory \t Free Memory  \t Total Memory \t Max Memory");
        System.out.println("\t " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/mb +
                " \t \t " + Runtime.getRuntime().freeMemory()/mb +
                " \t \t " + Runtime.getRuntime().totalMemory()/mb +
                " \t \t " + Runtime.getRuntime().maxMemory()/mb);
    }
}