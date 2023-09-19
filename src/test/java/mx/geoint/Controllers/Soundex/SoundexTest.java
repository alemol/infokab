package mx.geoint.Controllers.Soundex;

import mx.geoint.Model.Soundex.SoundexResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SoundexTest {
    @Test
    void testSound(){
        Soundex soundex = new Soundex();
        String code = "", word = "";
        String[] arr = new String[1];

        arr[0] = "próomosyonaartik";

        for(int i=0;i<arr.length; i++){
            System.out.println("****");
            SoundexResponse soundexResponse = soundex.maya_soundex(arr[i]);
            System.out.println(arr[i] + " Codigo -> " + soundexResponse.getCode());
        }
    }
}