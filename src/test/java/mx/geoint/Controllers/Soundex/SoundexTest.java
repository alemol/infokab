package mx.geoint.Controllers.Soundex;

import mx.geoint.Model.Soundex.SoundexResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SoundexTest {
    @Test
    void testSound(){
        Soundex soundex = new Soundex();
        String code = "", word = "";
        String[] arr = new String[4];

        arr[0] = "ti'i’al";
        arr[1] = "tya’al";
        arr[2] = "ta’’ap’'";
        arr[3] = "ttttta’’ap’'";

        for(int i=0;i<arr.length; i++){
            System.out.println("****");
            SoundexResponse soundexResponse = soundex.maya_soundex(arr[i]);
            System.out.println(arr[i] + " Codigo -> " + soundexResponse.getCode());
        }
    }
}