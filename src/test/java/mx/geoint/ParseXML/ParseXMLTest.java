package mx.geoint.ParseXML;

import org.junit.jupiter.api.Test;

import java.text.Normalizer;

import static org.junit.jupiter.api.Assertions.*;
class ParseXMLTest {
    private ParseXML parseXML;

    public void initParseXML(String tier_id){
        String original_path_name = "src/main/resources/2015-01-09_1650_Entrevista_datos_espontáneos_Clementina.eaf";
        String normalize = Normalizer.normalize(original_path_name, Normalizer.Form.NFD);
        String path_name = normalize.replaceAll("[^\\p{ASCII}]", "");
        parseXML = new ParseXML(path_name, tier_id);

    }

    @Test
    void read() {
        initParseXML("traduccion");
        assertSame("src/main/resources/eligio_uikab_mena.eaf",parseXML.filePath);
    }

    @Test
    void getInitTier() {
        initParseXML("traduccion");
        assertAll(
                () -> assertNull(parseXML.getTier())
        );
    }

    @Test
    void getTierTranscription() {
        //{
        // "ANNOTATION_ID":"a5",
        // "TIME_SLOT_REF1":"ts1",
        // "TIME_VALUE1":"34240",
        // "TIME_SLOT_REF2":"ts5",
        // "TIME_VALUE2":"39000",
        // "ANNOTATION_VALUE":"Le tomojchi’o’, tak le chéen u yawat le kaaxo’ob, yéetel áak’abo’,",
        // "DIFF_TIME":4760.0
        // }
        initParseXML("traduccion");
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
    void getTierTraduccion() {
        //{
        // "ANNOTATION_ID":"a14",
        // "TIME_SLOT_REF1":"ts2",
        // "TIME_VALUE1":"34240",
        // "TIME_SLOT_REF2":"ts6",
        // "TIME_VALUE2":"39000",
        // "ANNOTATION_VALUE":"El mal agüero se da incluso con el puro grito de las gallinas por la noche,",
        // "DIFF_TIME":4760.0
        // }
        initParseXML("Traducción");
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
    void getTierGlosado() {//{
        // "ANNOTATION_ID":"a23",
        // "TIME_SLOT_REF1":"ts3",
        // "TIME_VALUE1":"34240",
        // "TIME_SLOT_REF2":"ts7",
        // "TIME_VALUE2":"39000",
        // "ANNOTATION_VALUE":"DM mal.agüero=D2 hasta DM sólo ERG.3SG EP-grito DM gallina-PL con-ABS.3SG noche=D2",
        // "DIFF_TIME":4760.0
        // }

        initParseXML("Glosado");
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
    void getTierMorfemas() {
        // "ANNOTATION_ID":"a32",
        // "TIME_SLOT_REF1":"ts4",
        // "TIME_VALUE1":"34240",
        // "TIME_SLOT_REF2":"ts8",
        // "TIME_VALUE2":"39000",
        // "ANNOTATION_VALUE":"Le tomojchi’=o’, tak le chéen u y-awat le kaax-o’ob, yéetel-ø áak’ab=o’,",
        // "DIFF_TIME":4760.0
        // }
        initParseXML("Morfemas");
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
}