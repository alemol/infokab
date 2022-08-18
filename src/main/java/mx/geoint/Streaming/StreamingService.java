package mx.geoint.Streaming;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class StreamingService {
    private static final String FORMAT = "classpath:public/Files/multimedia/%s.mp4";
    private static final String AUDIO_FORMAT = "classpath:public/Files/multimedia/%s.wav";

    private static final String VIDEO_FORMAT_V2 = "url:http://localhost:8080/multimedia/%s.mp4";
    private static final String AUDIO_FORMAT_V2 = "url:http://localhost:8080/multimedia/%s.wav";

    @Autowired
    private ResourceLoader resourceLoader;

    public Mono<Resource> getVideo(String title){
        return Mono.fromSupplier(() -> this.resourceLoader.getResource(String.format(FORMAT, title)));
    }

    public Mono<Resource> getVideoV2(String title){
        return Mono.fromSupplier(() -> this.resourceLoader.getResource(String.format(VIDEO_FORMAT_V2, title)));
    }

    public Mono<Resource> getAudio(String title){
        return Mono.fromSupplier(() -> this.resourceLoader.getResource(String.format(AUDIO_FORMAT, title)));
    }

    public Mono<Resource> getAudioV2(String title){
        return Mono.fromSupplier(() -> this.resourceLoader.getResource(String.format(AUDIO_FORMAT_V2, title)));
    }
}
