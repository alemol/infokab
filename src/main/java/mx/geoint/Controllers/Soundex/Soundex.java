package mx.geoint.Controllers.Soundex;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class Soundex {
    Dictionary<String, String> priorclass3 = new Hashtable<>();
    Dictionary<String, String> priorclass2 = new Hashtable<>();
    Dictionary<String, String> priorclass1 = new Hashtable<>();

    Soundex(){
        initPriorClass3();
        initPriorClass2();
        initPriorClass1();
    }

    String split(String input_string, Dictionary<String, String> priordict){
        String output_string = input_string;
        for (Enumeration k = priordict.keys(); k.hasMoreElements();)
        {
            String key = (String) k.nextElement();
            //System.out.println("Keys in Dictionary : " + key + " -> " +priordict.get(key));
            output_string = output_string.replaceAll(key, priordict.get(key));
        }
        return output_string;
    }

    String scrap(String input_string, Dictionary<String, String> priordict){
        String output_string = "";

        for (int i=0; i < input_string.length(); i++){
            String character = String.valueOf(input_string.charAt(i));
            String value = priordict.get(character);
            if(value != null){
                output_string += character;
            }
        }

        return output_string;
    }

    String maya_soundex(String input_string){
        String modif_string = "";

        System.out.println("Original -> " + input_string);
        modif_string  = split(input_string, priorclass3);
        System.out.println("priorclass3 -> " + modif_string);

        modif_string = split(modif_string, priorclass2);
        System.out.println("priorclass2 -> " + modif_string);

        modif_string = scrap(modif_string, priorclass1);
        System.out.println("scrap with priorclass1 -> " + modif_string);

        modif_string = split(modif_string, priorclass1);
        System.out.println("priorclass1 -> " + modif_string);

        String code = (input_string.toUpperCase().charAt(0) + modif_string + "0000000000").substring(0,10);
        return code;
    }

    void initPriorClass3(){
        //Codigos del fila 0
        priorclass3.put("a'a", "a");
        priorclass3.put("e'e", "e");
        priorclass3.put("o'o", "o");
        priorclass3.put("u'u", "u");

        //Codigos del fila 2
        priorclass3.put("ch'", "x");

        //Codigos del fila 5
        priorclass3.put("i'i", "i");

        //Codigos del fila 7
        priorclass3.put("ts'", "s");

        //Codigos del fila 8
        priorclass3.put("que", "qe");
        priorclass3.put("qui", "qi");
    }

    void initPriorClass2(){
        //Codigos del fila 0
        priorclass2.put("aa", "a");
        priorclass2.put("áa", "a");
        priorclass2.put("a'", "a");

        priorclass2.put("ee", "e");
        priorclass2.put("ée", "e");
        priorclass2.put("e'", "e");

        priorclass2.put("oo", "o");
        priorclass2.put("óo", "o");
        priorclass2.put("o'", "o");

        priorclass2.put("uu", "u");
        priorclass2.put("úu", "u");
        priorclass2.put("u'", "u");

        //Codigos del fila 1
        priorclass2.put("p'", "v");

        //Codigos del fila 2
        priorclass2.put("ch", "x");
        priorclass2.put("sh", "x");

        //Codigos del fila 3
        priorclass2.put("t'", "d");

        //Codigos del fila 4
        priorclass2.put("gu'", "w");
        priorclass2.put("gw'", "w");

        //Codigos del fila 5
        priorclass2.put("ii", "i");
        priorclass2.put("íi", "i");
        priorclass2.put("i'", "i");
        priorclass2.put("ll", "y");

        //Codigos del fila 6
        priorclass2.put("rr", "r");

        //Codigos del fila 7
        priorclass2.put("ts", "s");
        priorclass2.put("tz", "s");
        priorclass2.put("dz", "s");
        priorclass2.put("ks", "s");
        priorclass2.put("cs", "s");

        //Codigos del fila 8
        priorclass2.put("k'", "k");
        priorclass2.put("que", "k");
        priorclass2.put("qui", "k");
        priorclass2.put("ng", "k");
        priorclass2.put("c", "k");
        priorclass2.put("ca", "ka");
        priorclass2.put("ce", "ke");
        priorclass2.put("ci", "ki");
        priorclass2.put("co", "ko");
        priorclass2.put("cu", "ku");
        priorclass2.put("cá", "ká");
        priorclass2.put("cé", "ké");
        priorclass2.put("cí", "kí");
        priorclass2.put("có", "kó");
        priorclass2.put("cú", "kí");

        //Codigos del fila 10
        priorclass2.put("ny'", "ñ");
        priorclass2.put("ni", "ñ");
    }

    void initPriorClass1(){
        //Codigos del fila 0
        priorclass1.put("a", "");
        priorclass1.put("á", "");
        priorclass1.put("é", "");
        priorclass1.put("e", "");
        priorclass1.put("ó", "");
        priorclass1.put("o", "");
        priorclass1.put("ú", "");
        priorclass1.put("u", "");

        //Codigos del fila 1
        priorclass1.put("b", "1");
        priorclass1.put("p", "1");
        priorclass1.put("v", "1");
        priorclass1.put("f", "1");

        //Codigos del fila 2
        priorclass1.put("x", "2");

        //Codigos del fila 3
        priorclass1.put("d", "3");
        priorclass1.put("t", "3");

        //Codigos del fila 4
        priorclass1.put("g", "4");
        priorclass1.put("h", "4");
        priorclass1.put("j", "4");
        priorclass1.put("u", "4");
        priorclass1.put("ü", "4");
        priorclass1.put("w", "4");

        //Codigos del fila 5
        priorclass1.put("i", "5");
        priorclass1.put("í", "5");
        priorclass1.put("y", "5");

        //Codigos del fila 6
        priorclass1.put("r", "6");
        priorclass1.put("l", "6");

        //Codigos del fila 7
        priorclass1.put("s", "7");
        priorclass1.put("z", "7");

        //Codigos del fila 8
        priorclass1.put("k", "8");

        //Codigos del fila 9
        priorclass1.put("n", "9");
        priorclass1.put("m", "9");

        //Codigos del fila A
        priorclass1.put("ñ", "a");
    }

}
