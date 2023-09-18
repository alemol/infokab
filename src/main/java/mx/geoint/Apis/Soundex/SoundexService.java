package mx.geoint.Apis.Soundex;

import mx.geoint.Controllers.Soundex.Soundex;
import mx.geoint.Model.Search.SearchResponse;
import mx.geoint.Model.Soundex.SoundexResponse;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;

@Service
public class SoundexService {
    public SoundexResponse getWord(String word)  {
        Soundex soundex = new Soundex();
        SoundexResponse soundexResponse= soundex.maya_soundex(word);
        return soundexResponse;
    }

}
