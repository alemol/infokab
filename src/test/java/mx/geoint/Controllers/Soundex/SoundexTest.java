package mx.geoint.Controllers.Soundex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SoundexTest {
    @Test
    void testSound(){
        Soundex soundex = new Soundex();
        String code = "", word = "";
        String[] arr = new String[3];

        arr[0] = "ti'i’al";
        arr[1] = "tya’al";
        arr[2] = "ta’’ap’'";

        for(int i=0;i<arr.length; i++){
            System.out.println("****");
            code = soundex.maya_soundex(arr[i]);
            System.out.println(arr[i] + " Codigo -> " + code);
        }
    }
}