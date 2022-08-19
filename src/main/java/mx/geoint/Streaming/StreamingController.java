package mx.geoint.Streaming;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.HandlerMapping;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = {"http://localhost:3000", "http://10.2.102.202:3000/", "http://10.2.102.182"})
@RestController
public class StreamingController {
    @Autowired
    private StreamingService streamingService;

    @GetMapping(value = "video/{title}", produces = "video/mp4")
    public Mono<Resource> getVideo(@PathVariable String title, @RequestHeader("Range") String range){
        System.out.println(range);
        return streamingService.getVideo(title);
    }
    @GetMapping(value = "videoV2/**", produces = "video/mp4")
    public Mono<Resource> getVideoV2(@RequestHeader("Range") String range, HttpServletRequest request){
        System.out.println(range);
        String urlTail = new AntPathMatcher().extractPathWithinPattern( "videoV2/**", request.getRequestURI() );
        return streamingService.getVideoV2(urlTail);
    }

    @GetMapping(value = "audio/{title}", produces = "audio/wav")
    public Mono<Resource> getAudio(@PathVariable String title, @RequestHeader("Range") String range){
        System.out.println(range);
        return streamingService.getAudio(title);
    }

    @GetMapping(value = "audioV2/**", produces = "audio/wav")
    public Mono<Resource> getAudioV2(@RequestHeader("Range") String range, HttpServletRequest request){
        System.out.println(range);
        String urlTail = new AntPathMatcher().extractPathWithinPattern( "audiov2/**", request.getRequestURI() );
        return streamingService.getAudioV2(urlTail);
    }
}
