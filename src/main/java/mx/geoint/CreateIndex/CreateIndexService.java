package mx.geoint.CreateIndex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CreateIndexService {
    @Autowired
    CreateIndex createIndex;

    public String CreateIndexLucene(String text) throws IOException {
        return createIndex.createdIndex(text);
    }
}
