package mx.geoint.Streaming;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class StreamingController {
    @Autowired
    private StreamingService streamingService;

    @GetMapping(value = "video/{title}", produces = "video/mp4")
    public Mono<Resource> getVideo(@PathVariable String title, @RequestHeader("Range") String range){
        System.out.println(range);
        return streamingService.getVideo(title);
    }

    @GetMapping(value = "audio/{title}", produces = "audio/wav")
    public Mono<Resource> getAudio(@PathVariable String title, @RequestHeader("Range") String range){
        System.out.println(range);
        return streamingService.getAudio(title);
    }
}
