package mx.geoint.Controllers.Soundex;

import mx.geoint.Model.Soundex.SoundexResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SoundexTest {
    @Test
    void testSound(){
        Soundex soundex = new Soundex();
        String code = "", word = "";
        String[] arr = new String[20];

        arr[0] = "hanal";
        arr[1] = "janal";
        arr[2] = "saanaoryae";
        arr[3] = "saanaoryao";
        arr[4] = "zanahoria";
        arr[5] = "zanahori'a";
        arr[6] = "zanahorya";
        arr[7] = "zanaorya";
        arr[8] = "zanaoria";
        arr[9] = "zanaori'a";
        arr[10] = "sanahoria";
        arr[11] = "sanahori'a";
        arr[12] = "sanahorya";
        arr[13] = "sanaorya";
        arr[14] = "sanaoria";
        arr[15] = "sanaori'a";
        arr[16] = "zanahorlla";
        arr[17] = "zanaorlla";
        arr[18] = "sanahorlla";
        arr[19] = "sanaorlla";



        for(int i=0;i<arr.length; i++){
            System.out.println("****");
            SoundexResponse soundexResponse = soundex.maya_soundex(arr[i]);
            System.out.println(arr[i] + " Codigo -> " + soundexResponse.getEndCode());
        }
    }
}