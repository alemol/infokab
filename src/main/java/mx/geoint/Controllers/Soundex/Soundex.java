package mx.geoint.Controllers.Soundex;

import mx.geoint.Model.Soundex.SoundexResponse;

import java.util.*;

public class Soundex {
    SoundexResponse soundexResponse = new SoundexResponse();
    Dictionary<String, String> priorclass3 = new Hashtable<>();
    Dictionary<String, String> priorclass2 = new Hashtable<>();
    Dictionary<String, String> priorclass1 = new Hashtable<>();
    Dictionary<String, String> representants  = new Hashtable<>();
    public Soundex(){
        initPriorClass3();
        initPriorClass2();
        initPriorClass1();
        initRepresentants();
    }

    /**
     * Remplaza los caracteres de una cadena de texto que se encuentre en el diccionario priordict
     * @param input_string Cadena de texto a analizar
     * @param priordict Diccionario que contiene las palabras a remplazar
     * @return
     */
    String hashtable(String input_string, Dictionary<String, String> priordict){
        String output_string = input_string;
        for (Enumeration k = priordict.keys(); k.hasMoreElements();)
        {
            String key = (String) k.nextElement();
            //System.out.println("Keys in Dictionary : " + key + " -> " +priordict.get(key));
            output_string = output_string.replaceAll(key, priordict.get(key));
        }
        return output_string;
    }

    /**
     * Elimina los caracteres repetidos consecutivamente (aaa) -> (a)
     * @param input_string Cadena de texto a analizar
     * @return
     */
    String replace_repeated(String input_string){
        String output_string = "";
        String last_character = "";

        for(int i=0;i<input_string.length(); i++) {
            if(!last_character.equals(String.valueOf(input_string.charAt(i)))){
                last_character = String.valueOf(input_string.charAt(i));
                output_string += last_character;
            }
        }
        return output_string;
    }

    /**
     * Elimina los caracteres que no se encuentren en el diccionario priodict
     * @param input_string Cadena de texto a analizar
     * @param priordict Diccionario que contiene las palabras a remplazar
     * @return
     */
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

    String special_start(String input_string){
        String modif_string = "";
        input_string = input_string.toLowerCase();
        input_string = input_string.strip();

        if(input_string.length()>=2 && input_string.substring(0,2).equals("ll")){
            modif_string = "y"+input_string.substring(2);
        } else if (input_string.length()>=1 && input_string.substring(0,1).equals("h")) {
            modif_string = "j"+input_string.substring(1);
        } else {
            modif_string = input_string;
        }

        return modif_string;
    }

    public SoundexResponse maya_soundex(String input_string){
        String modif_string = "";
        String fistLetter = "";
        String code = "";

        modif_string = special_start(input_string);
        //System.out.println("Special Start -> " + modif_string);
        soundexResponse.setOriginalWord(input_string);

        modif_string  = hashtable(modif_string, priorclass3);
        //System.out.println("priorclass3 -> " + modif_string);
        soundexResponse.setPriorClass3(modif_string);

        modif_string = hashtable(modif_string, priorclass2);
        //System.out.println("priorclass2 -> " + modif_string);
        soundexResponse.setPriorClass2(modif_string);

        //NORMALIZAR
        modif_string = scrap(modif_string, priorclass1);
        //System.out.println("scrap with priorclass1 -> " + modif_string);
        soundexResponse.setScrap(modif_string);

        if(modif_string.length() == 0){
            soundexResponse.setEndCode("*************");
            return soundexResponse;
        }

        fistLetter = hashtable(String.valueOf(modif_string.charAt(0)), priorclass1);
        fistLetter = hashtable(fistLetter, representants);

        modif_string = hashtable(modif_string, priorclass1);
        //System.out.println("priorclass1 -> " + modif_string);

        modif_string = replace_repeated(modif_string);
        //System.out.println("replace Repeated -> " + modif_string);
        if(!modif_string.equals("")){
            code = (fistLetter + modif_string.substring(1) + "*************").substring(0,13);
        }
        soundexResponse.setEndCode(code);

        return soundexResponse;
    }

    public String preprocessing(String input_string){
        input_string.replaceAll(",|¿|\\?|\\[|\\]|«|»|<<|>>|\\(|\\)|\\.", "");
        return input_string;
    }

    public String get_maya_soundex(String phrase) {
        String prepro_phase = preprocessing(phrase);
        String[] list_words = prepro_phase.split(" ");
        List<String> code_list = new ArrayList<String>();

        for (String s : list_words) {
            String soundex_code = maya_soundex(s).getEndCode();
            code_list.add(soundex_code);
        }

        String encoded_phrase = String.join(" ", code_list);
        return encoded_phrase;
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
        //priorclass2.put("ks", "s");
        priorclass2.put("cs", "s");

        //Codigos del fila 8
        priorclass2.put("k'", "k");
        priorclass2.put("que", "k");
        priorclass2.put("qui", "k");
        priorclass2.put("ng", "k");
        //priorclass2.put("c", "k");
        priorclass2.put("ca", "ka");
        //priorclass2.put("ce", "ke");
        //priorclass2.put("ci", "ki");
        priorclass2.put("co", "ko");
        priorclass2.put("cu", "ku");
        priorclass2.put("cá", "ká");
        //priorclass2.put("cé", "ké");
        //priorclass2.put("cí", "kí");
        priorclass2.put("có", "kó");
        priorclass2.put("cú", "kú");

        //Codigos del fila 10
        priorclass2.put("ny'", "ni");
        priorclass2.put("ñ", "ni");
    }

    void initPriorClass1(){
        //Codigos del fila 0
        priorclass1.put("a", "0");
        priorclass1.put("á", "0");
        priorclass1.put("é", "0");
        priorclass1.put("e", "0");
        priorclass1.put("ó", "0");
        priorclass1.put("o", "0");
        //priorclass1.put("ú", "0");
        //priorclass1.put("u", "0");

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
        //priorclass1.put("h", "4");
        priorclass1.put("j", "4");
        priorclass1.put("u", "4");
        priorclass1.put("ü", "4");
        priorclass1.put("w", "4");
        priorclass1.put("ú", "4");
        priorclass1.put("u", "4");

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
        priorclass1.put("c", "8");

        //Codigos del fila 9
        priorclass1.put("n", "9");
        priorclass1.put("m", "9");

        //Codigos del fila A
        priorclass1.put("ñ", "A");
    }

    void initRepresentants(){
        representants.put("0", "A");
        representants.put("1", "B");
        representants.put("2", "X");
        representants.put("3", "T");
        representants.put("4", "U");
        representants.put("5", "Y");
        representants.put("6", "R");
        representants.put("7", "S");
        representants.put("8", "K");
        representants.put("9", "M");
        representants.put("A", "Ñ");
    }
}
