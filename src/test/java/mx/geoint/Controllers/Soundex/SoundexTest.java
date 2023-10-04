package mx.geoint.Controllers.Soundex;

import mx.geoint.Database.DBSearch;
import mx.geoint.Model.Search.SearchPostgres;
import mx.geoint.Model.Soundex.SoundexResponse;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SoundexTest {
    private DBSearch dbSearch;

    @Test
    void testSound(){
        Soundex soundex = new Soundex();
        List<String> testList = new ArrayList<String>();

        testList.add("hanal");
        testList.add("janal");
        testList.add("saanaoryae");
        testList.add("saanaoryao");
        testList.add("zanahoria");
        testList.add("zanahori'a");
        testList.add("zanahorya");
        testList.add("zanaorya");
        testList.add("zanaoria");
        testList.add("zanaori'a");
        testList.add("sanahoria");
        testList.add("sanahori'a");
        testList.add("sanahorya");
        testList.add("sanaorya");
        testList.add("sanaoria");
        testList.add("sanaori'a");
        testList.add("zanahorlla");
        testList.add("zanaorlla");
        testList.add("sanahorlla");
        testList.add("sanaorlla");

        for(int i=0;i<testList.size(); i++){
            System.out.println("****");
            SoundexResponse soundexResponse = soundex.maya_soundex(testList.get(i));
            System.out.println(testList.get(i) + " Codigo -> " + soundexResponse.getEndCode());
        }
    }

    @Test
    void testSoundWord(){
        Soundex soundex = new Soundex();
        List<String> testList = new ArrayList<String>();

        testList.add("man");
        testList.add("maan");
        testList.add("máan");
        testList.add("pek");
        testList.add("péek");
        testList.add("paalo'ob");
        testList.add("palob");
        testList.add("paalo'o'");
        testList.add("paaló");
        testList.add("ta'ab");
        testList.add("ta'ap");
        testList.add("xanab");
        testList.add("xana");
        testList.add("vena");
        testList.add("beena");
        testList.add("cafe");
        testList.add("kaape");
        testList.add("kaafe");
        testList.add("boox");
        testList.add("bosh");
        testList.add("wishar");
        testList.add("wichar");
        testList.add("xaman");
        testList.add("chaman");
        testList.add("shaman");
        testList.add("tat");
        testList.add("den");
        testList.add("ten");
        testList.add("honestidat");
        testList.add("taan");
        testList.add("t'aan");
        testList.add("genaro");
        testList.add("jenaaro");
        testList.add("hana");
        testList.add("jana");
        testList.add("guerra");
        testList.add("gera");
        testList.add("güera");
        testList.add("gweera");
        testList.add("weera");
        testList.add("bueno");
        testList.add("bweeno");
        testList.add("ti'al");
        testList.add("tya'al");
        testList.add("secretaria");
        testList.add("sekretaarya");
        testList.add("sekretariiya");
        testList.add("llave");
        testList.add("yaabe");
        testList.add("perro");
        testList.add("peero");
        testList.add("turix");
        testList.add("purux");
        testList.add("arux");
        testList.add("alux");
        testList.add("ts'uul");
        testList.add("papadzul");
        testList.add("tsaats");
        testList.add("oksej");
        testList.add("kang");
        testList.add("kank");
        testList.add("ki'ichpam");
        testList.add("jump'éel");
        testList.add("niño");
        testList.add("niinyo");
        testList.add("nieto");
        testList.add("nyeeto");

        for(int i=0;i<testList.size(); i++){
            System.out.println("****");
            SoundexResponse soundexResponse = soundex.maya_soundex(testList.get(i));
            System.out.println(testList.get(i) + " Codigo -> " + soundexResponse.getEndCode());
        }
    }

    @Test
    void testSoundDB() throws SQLException {
        Soundex soundex = new Soundex();
        this.dbSearch = new DBSearch();
        ArrayList<SearchPostgres> result = this.dbSearch.listSearch();

        for(int i=0;i<result.size(); i++){
            System.out.println("****");
            SoundexResponse soundexResponse = soundex.maya_soundex(result.get(i).getConsulta());
            System.out.println(result.get(i).getConsulta() + " Codigo -> " + soundexResponse.getEndCode());
        }
    }
}