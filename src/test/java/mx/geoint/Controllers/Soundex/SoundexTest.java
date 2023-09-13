package mx.geoint.Controllers.Soundex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SoundexTest {
    @Test
    void testSound(){
        Soundex soundex = new Soundex();
        String code = soundex.maya_soundex("taants'il");
        System.out.println("modif_string -> " + code);
    }
}