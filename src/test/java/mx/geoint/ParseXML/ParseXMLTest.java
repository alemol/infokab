package mx.geoint.ParseXML;

import com.google.gson.Gson;
import mx.geoint.Controllers.ParseXML.ParseXML;
import mx.geoint.Controllers.ParseXML.Tier;
import mx.geoint.Model.Glosado.GlosaStep;
import mx.geoint.pathSystem;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
class ParseXMLTest {
    private ParseXML parseXML;

    public void initParseXMLVideo(String tier_id){
        String original_path_name = "src/main/resources/2015-01-09_1650_Entrevista_datos_espontáneos_Clementina.eaf";
        String normalize = Normalizer.normalize(original_path_name, Normalizer.Form.NFD);
        String path_name = normalize.replaceAll("[^\\p{ASCII}]", "");
        parseXML = new ParseXML(path_name, tier_id);
    }

    public void initParseXMLAudio(String tier_id){
        String original_path_name = "src/main/resources/eligio_uikab_mena.eaf";
        String normalize = Normalizer.normalize(original_path_name, Normalizer.Form.NFD);
        String path_name = normalize.replaceAll("[^\\p{ASCII}]", "");
        parseXML = new ParseXML(path_name, tier_id);
    }

    @Test
    void getInitTierAudio() {
        initParseXMLAudio("traduccion");
        assertAll(
                () -> assertNull(parseXML.getTier())
        );
    }

    @Test
    void getInitTierVideo() {
        initParseXMLVideo("traduccion");
        assertAll(
                () -> assertNull(parseXML.getTier())
        );
    }

    @Test
    void getTierTranscriptionAudio() throws ParserConfigurationException, SAXException, IOException {
        //{
        // "ANNOTATION_ID":"a5",
        // "TIME_SLOT_REF1":"ts1",
        // "TIME_VALUE1":"34240",
        // "TIME_SLOT_REF2":"ts5",
        // "TIME_VALUE2":"39000",
        // "ANNOTATION_VALUE":"Le tomojchi’o’, tak le chéen u yawat le kaaxo’ob, yéetel áak’abo’,",
        // "DIFF_TIME":4760.0
        // }
        initParseXMLAudio("oracion");
        parseXML.read();

        assertAll(
            () -> assertEquals("a5", parseXML.getTier().get(0).ANNOTATION_ID),
            () -> assertEquals("ts1", parseXML.getTier().get(0).TIME_SLOT_REF1),
            () -> assertEquals("34240", parseXML.getTier().get(0).TIME_VALUE1),
            () -> assertEquals("ts5", parseXML.getTier().get(0).TIME_SLOT_REF2),
            () -> assertEquals("39000", parseXML.getTier().get(0).TIME_VALUE2),
            () -> assertEquals("Le tomojchi’o’, tak le chéen u yawat le kaaxo’ob, yéetel áak’abo’,", parseXML.getTier().get(0).ANNOTATION_VALUE),
            () -> assertEquals(4760.0, parseXML.getTier().get(0).DIFF_TIME)
        );
    }

    @Test
    void getTierTraduccionAudio() throws ParserConfigurationException, SAXException, IOException{
        //{
        // "ANNOTATION_ID":"a14",
        // "TIME_SLOT_REF1":"ts2",
        // "TIME_VALUE1":"34240",
        // "TIME_SLOT_REF2":"ts6",
        // "TIME_VALUE2":"39000",
        // "ANNOTATION_VALUE":"El mal agüero se da incluso con el puro grito de las gallinas por la noche,",
        // "DIFF_TIME":4760.0
        // }
        initParseXMLAudio("Traducción");
        parseXML.read();
        assertAll(
                () -> assertEquals("a14", parseXML.getTier().get(0).ANNOTATION_ID),
                () -> assertEquals("ts2", parseXML.getTier().get(0).TIME_SLOT_REF1),
                () -> assertEquals("34240", parseXML.getTier().get(0).TIME_VALUE1),
                () -> assertEquals("ts6", parseXML.getTier().get(0).TIME_SLOT_REF2),
                () -> assertEquals("39000", parseXML.getTier().get(0).TIME_VALUE2),
                () -> assertEquals("El mal agüero se da incluso con el puro grito de las gallinas por la noche,", parseXML.getTier().get(0).ANNOTATION_VALUE),
                () -> assertEquals(4760.0, parseXML.getTier().get(0).DIFF_TIME)
        );
    }

    @Test
    void getTierGlosadoAudio() throws ParserConfigurationException, SAXException, IOException {//{
        // "ANNOTATION_ID":"a23",
        // "TIME_SLOT_REF1":"ts3",
        // "TIME_VALUE1":"34240",
        // "TIME_SLOT_REF2":"ts7",
        // "TIME_VALUE2":"39000",
        // "ANNOTATION_VALUE":"DM mal.agüero=D2 hasta DM sólo ERG.3SG EP-grito DM gallina-PL con-ABS.3SG noche=D2",
        // "DIFF_TIME":4760.0
        // }

        initParseXMLAudio("Glosado");
        parseXML.read();
        assertAll(
                () -> assertEquals("a23", parseXML.getTier().get(0).ANNOTATION_ID),
                () -> assertEquals("ts3", parseXML.getTier().get(0).TIME_SLOT_REF1),
                () -> assertEquals("34240", parseXML.getTier().get(0).TIME_VALUE1),
                () -> assertEquals("ts7", parseXML.getTier().get(0).TIME_SLOT_REF2),
                () -> assertEquals("39000", parseXML.getTier().get(0).TIME_VALUE2),
                () -> assertEquals("DM mal.agüero=D2 hasta DM sólo ERG.3SG EP-grito DM gallina-PL con-ABS.3SG noche=D2", parseXML.getTier().get(0).ANNOTATION_VALUE),
                () -> assertEquals(4760.0, parseXML.getTier().get(0).DIFF_TIME)
        );
    }

    @Test
    void getTierMorfemasAudio() throws ParserConfigurationException, SAXException, IOException {
        // "ANNOTATION_ID":"a32",
        // "TIME_SLOT_REF1":"ts4",
        // "TIME_VALUE1":"34240",
        // "TIME_SLOT_REF2":"ts8",
        // "TIME_VALUE2":"39000",
        // "ANNOTATION_VALUE":"Le tomojchi’=o’, tak le chéen u y-awat le kaax-o’ob, yéetel-ø áak’ab=o’,",
        // "DIFF_TIME":4760.0
        // }
        initParseXMLAudio("Morfemas");
        parseXML.read();
        assertAll(
                () -> assertEquals("a32", parseXML.getTier().get(0).ANNOTATION_ID),
                () -> assertEquals("ts4", parseXML.getTier().get(0).TIME_SLOT_REF1),
                () -> assertEquals("34240", parseXML.getTier().get(0).TIME_VALUE1),
                () -> assertEquals("ts8", parseXML.getTier().get(0).TIME_SLOT_REF2),
                () -> assertEquals("39000", parseXML.getTier().get(0).TIME_VALUE2),
                () -> assertEquals("Le tomojchi’=o’, tak le chéen u y-awat le kaax-o’ob, yéetel-ø áak’ab=o’,", parseXML.getTier().get(0).ANNOTATION_VALUE),
                () -> assertEquals(4760.0, parseXML.getTier().get(0).DIFF_TIME)
        );
    }

    //SECTION DE TEST VIDEO
    @Test
    void getTierTranscriptionVideo() throws ParserConfigurationException, SAXException, IOException {
        //{
        // "ANNOTATION_ID":"a1",
        // "TIME_SLOT_REF1":"ts2",
        // "TIME_VALUE1":"21340",
        // "TIME_SLOT_REF2":"ts4",
        // "TIME_VALUE2":"22640",
        // "ANNOTATION_VALUE":"Taase aak'o' kinjats'ej"
        // "DIFF_TIME":1300.0
        // }
        initParseXMLVideo("oracion");
        parseXML.read();

        assertAll(
                () -> assertEquals("a1", parseXML.getTier().get(0).ANNOTATION_ID),
                () -> assertEquals("ts2", parseXML.getTier().get(0).TIME_SLOT_REF1),
                () -> assertEquals("21340", parseXML.getTier().get(0).TIME_VALUE1),
                () -> assertEquals("ts4", parseXML.getTier().get(0).TIME_SLOT_REF2),
                () -> assertEquals("22640", parseXML.getTier().get(0).TIME_VALUE2),
                () -> assertEquals("Taase aak'o' kinjats'ej", parseXML.getTier().get(0).ANNOTATION_VALUE),
                () -> assertEquals(1300, parseXML.getTier().get(0).DIFF_TIME)
        );
    }

    @Test
    void getTierTraduccionVideo() throws ParserConfigurationException, SAXException, IOException {
        //{
        // "ANNOTATION_ID":"a13",
        // "TIME_SLOT_REF1":"ts2",
        // "TIME_VALUE1":"21340",
        // "TIME_SLOT_REF2":"ts4",
        // "TIME_VALUE2":"22640",
        // "ANNOTATION_VALUE":"Trae el bejuco para que la pegue",
        // "DIFF_TIME":1300.0
        // }
        initParseXMLVideo("Traducción");
        parseXML.read();
        assertAll(
                () -> assertEquals("a13", parseXML.getTier().get(0).ANNOTATION_ID),
                () -> assertEquals("ts2", parseXML.getTier().get(0).TIME_SLOT_REF1),
                () -> assertEquals("21340", parseXML.getTier().get(0).TIME_VALUE1),
                () -> assertEquals("ts4", parseXML.getTier().get(0).TIME_SLOT_REF2),
                () -> assertEquals("22640", parseXML.getTier().get(0).TIME_VALUE2),
                () -> assertEquals("Trae el bejuco para que la pegue", parseXML.getTier().get(0).ANNOTATION_VALUE),
                () -> assertEquals(1300.0, parseXML.getTier().get(0).DIFF_TIME)
        );
    }

    @Test
    void getTierGlosadoVideo() throws ParserConfigurationException, SAXException, IOException {//{
        // "ANNOTATION_ID":"a9",
        // "TIME_SLOT_REF1":"ts2",
        // "TIME_VALUE1":"21340",
        // "TIME_SLOT_REF2":"ts4",
        // "TIME_VALUE2":"22640",
        // "ANNOTATION_VALUE":"Traer-DET bejuco-? HAB-1sg.A-pegar-? ",
        // "DIFF_TIME":1300.0
        // }

        initParseXMLVideo("Glosado");
        parseXML.read();
        assertAll(
                () -> assertEquals("a9", parseXML.getTier().get(0).ANNOTATION_ID),
                () -> assertEquals("ts2", parseXML.getTier().get(0).TIME_SLOT_REF1),
                () -> assertEquals("21340", parseXML.getTier().get(0).TIME_VALUE1),
                () -> assertEquals("ts4", parseXML.getTier().get(0).TIME_SLOT_REF2),
                () -> assertEquals("22640", parseXML.getTier().get(0).TIME_VALUE2),
                () -> assertEquals("Traer-DET bejuco-? HAB-1sg.A-pegar-? ", parseXML.getTier().get(0).ANNOTATION_VALUE),
                () -> assertEquals(1300.0, parseXML.getTier().get(0).DIFF_TIME)
        );
    }

    @Test
    void getTierMorfemasVideo() throws ParserConfigurationException, SAXException, IOException {
        // "ANNOTATION_ID":"a5",
        // "TIME_SLOT_REF1":"ts2",
        // "TIME_VALUE1":"21340",
        // "TIME_SLOT_REF2":"ts4",
        // "TIME_VALUE2":"22640",
        // "ANNOTATION_VALUE":"Taas-e aak'-o' k-in-jats'ej",
        // "DIFF_TIME":1300.0
        // }
        initParseXMLVideo("Morfemas");
        parseXML.read();
        assertAll(
                () -> assertEquals("a5", parseXML.getTier().get(0).ANNOTATION_ID),
                () -> assertEquals("ts2", parseXML.getTier().get(0).TIME_SLOT_REF1),
                () -> assertEquals("21340", parseXML.getTier().get(0).TIME_VALUE1),
                () -> assertEquals("ts4", parseXML.getTier().get(0).TIME_SLOT_REF2),
                () -> assertEquals("22640", parseXML.getTier().get(0).TIME_VALUE2),
                () -> assertEquals("Taas-e aak'-o' k-in-jats'ej", parseXML.getTier().get(0).ANNOTATION_VALUE),
                () -> assertEquals(1300.0, parseXML.getTier().get(0).DIFF_TIME)
        );
    }

     void getTierDinamic(String tier_id, String path_eaf) throws ParserConfigurationException, SAXException, IOException{
        String original_path_name = path_eaf;
        String normalize = Normalizer.normalize(original_path_name, Normalizer.Form.NFD);
        String path_name = normalize.replaceAll("[^\\p{ASCII}]", "");
        ParseXML parseXML = new ParseXML(path_name, tier_id);
        parseXML.read();

        String jsonString = new Gson().toJson(parseXML.getTier());
         System.out.println(tier_id +" Size: " + parseXML.getTier().size());
        System.out.println(jsonString);
    }

    void testXMLFiles(String number_case) throws ParserConfigurationException, IOException, TransformerException, SAXException {
        String basePath = "./eafs/";
        String path = "";
        String tier_id_fonetico = "";
        String tier_id_transcripcion = "";
        String tier_id_traduccion = "";
        String tier_id_glosado = "Glosado";

        switch (number_case){
            case "1":
                path = "04_02_01072022_11_SCY_C_2_2.eaf";
                tier_id_fonetico= "Transcripción Fonético ";
                tier_id_transcripcion = "Transcripción Ortográfico";


                System.out.println(path);
                getTierDinamic(tier_id_fonetico, basePath+path);
                getTierDinamic(tier_id_transcripcion, basePath+path);
                getTierDinamic(tier_id_glosado, basePath+path);
                break;
            case "2":
                path = "04_02_01062022_11_SCY_C_2_2.eaf";
                tier_id_transcripcion = "Transcripcion ";
                tier_id_traduccion = "Traduccion";

                System.out.println(path);
                getTierDinamic(tier_id_transcripcion, basePath+path);
                getTierDinamic(tier_id_traduccion, basePath+path);
                getTierDinamic(tier_id_glosado, basePath+path);
                break;
            case "3":
                path = "04_02_01082022_11_SCY_C_2_2.eaf";
                tier_id_fonetico= "trascripcion fonetica";
                tier_id_transcripcion = "transpcion ortografica";
                tier_id_traduccion = "traduccion libre";

                System.out.println(path);
                getTierDinamic(tier_id_fonetico, basePath+path);
                getTierDinamic(tier_id_transcripcion, basePath+path);
                getTierDinamic(tier_id_traduccion, basePath+path);
                getTierDinamic(tier_id_glosado, basePath+path);
                break;
            case "4":
                path = "04_02_01092022_11_SCY_C_2_2.eaf";
                tier_id_fonetico= "morfo";
                tier_id_transcripcion = "oracion";
                tier_id_traduccion = "traduccion";

                System.out.println(path);
                getTierDinamic(tier_id_fonetico, basePath+path);
                getTierDinamic(tier_id_transcripcion, basePath+path);
                getTierDinamic(tier_id_traduccion, basePath+path);
                getTierDinamic(tier_id_glosado, basePath+path);
                break;
        }
    }

    @Test
    void runTestFile() throws ParserConfigurationException, IOException, TransformerException, SAXException {
        testXMLFiles("1");
        testXMLFiles("2");
        testXMLFiles("3");
        testXMLFiles("4");
    }

    void setTierDinamic(String tier_id, String path_eaf) throws ParserConfigurationException, IOException, TransformerException, SAXException {
        ArrayList<GlosaStep> glosaSteps = new ArrayList<>();
        glosaSteps.add(new GlosaStep(1, "to'one'", "to'one'", null, "(to'on)+e' = 1PL[to'on]+D3/TOP/FF"));
        glosaSteps.add(new GlosaStep(2, "ma'alobon", "ma'alobon", null, null));
        glosaSteps.add(new GlosaStep(3, "bejla'e'", "bejla'e'",null, "(bejla'e') = hoy[bejla'e']"));

        ParseXML parseXML = new ParseXML(path_eaf, tier_id);
        parseXML.writeElement("a283", "a284", glosaSteps);
    }

    @Test
    void runTestAddAnnotationInFile() throws ParserConfigurationException, IOException, TransformerException, SAXException {
        String path = "04_02_01082022_11_SCY_C_2_2.eaf";
        String tier_id_fonetico= "Transcripción Fonético ";
        String tier_id_transcripcion = "Transcripción Ortográfico";

        setTierDinamic(tier_id_transcripcion,"./eafs/"+path);
    }


    void getMapTier(String path_eaf) throws ParserConfigurationException, IOException, SAXException {
        String original_path_name = path_eaf;
        String normalize = Normalizer.normalize(original_path_name, Normalizer.Form.NFD);
        String path_name = normalize.replaceAll("[^\\p{ASCII}]", "");
        ParseXML parseXML = new ParseXML(path_name);
        parseXML.read();

        Map<String, List<Tier>> tiersList = parseXML.getTiers();
        for (var entry : tiersList.entrySet()){
            String jsonString = new Gson().toJson(entry.getValue());
            System.out.println(entry.getKey() +" Size: " + entry.getValue().size());
            System.out.println(jsonString);
        }
    }

    void testMapXMLFiles(String number_case) throws ParserConfigurationException, IOException, TransformerException, SAXException {
        String basePath = "./eafs/";
        String path = "";

        switch (number_case){
            case "1":
                path = "04_02_01072022_11_SCY_C_2_2.eaf";

                System.out.println(path);
                getMapTier(basePath+path);
                break;
            case "2":
                path = "04_02_01062022_11_SCY_C_2_2.eaf";

                System.out.println(path);
                getMapTier(basePath+path);
                break;
            case "3":
                path = "04_02_01082022_11_SCY_C_2_2.eaf";

                System.out.println(path);
                getMapTier(basePath+path);
                break;
            case "4":
                path = "04_02_01092022_11_SCY_C_2_2.eaf";

                System.out.println(path);
                getMapTier(basePath+path);
                break;
        }
    }

    @Test
    void runMapTestFile() throws ParserConfigurationException, IOException, TransformerException, SAXException {
        testMapXMLFiles("1");
        testMapXMLFiles("2");
        testMapXMLFiles("3");
        testMapXMLFiles("4");
    }

    void changeAnnotationinTier(String tier_id, String path_eaf) throws ParserConfigurationException, IOException, TransformerException, SAXException {
        ParseXML parseXML = new ParseXML(path_eaf, tier_id);
        parseXML.editAnnotation("a284",  "to'one' ma'alobon bejla'e'  chéen yaan horaa beya''", pathSystem.TIER_MAIN);
        parseXML.editAnnotation("a55",  "kek  bin xan jaan", pathSystem.TIER_MAIN);
    }

    @Test
    void runTestChangeAnnotationInFile() throws ParserConfigurationException, IOException, TransformerException, SAXException {
        String path = "04_02_01082022_11_SCY_C_2_2.eaf";
        String tier_id_transcripcion = "transpcion ortografica";

        changeAnnotationinTier(tier_id_transcripcion,"./eafs/"+path);
    }
}