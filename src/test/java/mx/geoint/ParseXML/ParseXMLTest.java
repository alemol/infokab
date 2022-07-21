package mx.geoint.ParseXML;

import org.junit.jupiter.api.Test;

import java.text.Normalizer;

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
    void getTierTranscriptionAudio() {
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
    void getTierTraduccionAudio() {
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
    void getTierGlosadoAudio() {//{
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
    void getTierMorfemasAudio() {
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
    void getTierTranscriptionVideo() {
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
    void getTierTraduccionVideo() {
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
    void getTierGlosadoVideo() {//{
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
    void getTierMorfemasVideo() {
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
}