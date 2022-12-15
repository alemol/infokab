package mx.geoint.Apis.CreateIndex;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path = "api/create")
public class CreateIndexController {
    private final CreateIndexService createIndexService;

    public CreateIndexController(CreateIndexService createIndexService) {
        this.createIndexService = createIndexService;
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String created(@RequestBody String text) throws IOException {
        return createIndexService.CreateIndexLucene(text);
    }
}
