package mx.geoint.Apis.Soundex;

import mx.geoint.Controllers.Soundex.Soundex;
import mx.geoint.Model.Soundex.SoundexResponse;
import org.springframework.stereotype.Service;

@Service
public class SoundexService {
    public SoundexResponse getWord(String word)  {
        Soundex soundex = new Soundex();
        SoundexResponse soundexResponse= soundex.maya_soundex(word);
        return soundexResponse;
    }

    public String getWords(String phrase)  {
        Soundex soundex = new Soundex();
        String soundexResponse= soundex.get_maya_soundex(phrase);
        return soundexResponse;
    }

}
