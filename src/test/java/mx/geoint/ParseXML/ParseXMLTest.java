package mx.geoint.ParseXML;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import mx.geoint.ParseXML.Tier;

import java.util.ArrayList;
import java.util.List;

class ParseXMLTest {
    private ParseXML parseXML;
    private List<Tier> transcripcion;
    private List<Tier> traduccion;
    private List<Tier> glosado;
    private List<Tier> morfemas;

    public void initParseXML(){
        parseXML = new ParseXML("src/main/resources/eligio_uikab_mena.eaf");
        transcripcion = new ArrayList<>();
        traduccion = new ArrayList<>();
        glosado = new ArrayList<>();
        morfemas = new ArrayList<>();

    }

    @Test
    void read() {
        initParseXML();
        assertTrue("src/main/resources/eligio_uikab_mena.eaf" == parseXML.filePath);
    }

    @Test
    void getInitTier() {
        initParseXML();
        assertAll(
                () -> assertEquals(null, parseXML.getTierTranscription()),
                () -> assertEquals(null, parseXML.getTierTraduccion()),
                () -> assertEquals(null, parseXML.getTierGlosado()),
                () -> assertEquals(null, parseXML.getTierMorfemas())
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
        initParseXML();
        parseXML.read();
        assertAll(
            () -> assertEquals("a5", parseXML.getTierTranscription().get(0).ANNOTATION_ID),
            () -> assertEquals("ts1", parseXML.getTierTranscription().get(0).TIME_SLOT_REF1),
            () -> assertEquals("34240", parseXML.getTierTranscription().get(0).TIME_VALUE1),
            () -> assertEquals("ts5", parseXML.getTierTranscription().get(0).TIME_SLOT_REF2),
            () -> assertEquals("39000", parseXML.getTierTranscription().get(0).TIME_VALUE2),
            () -> assertEquals("Le tomojchi’o’, tak le chéen u yawat le kaaxo’ob, yéetel áak’abo’,", parseXML.getTierTranscription().get(0).ANNOTATION_VALUE),
            () -> assertEquals(4760.0, parseXML.getTierTranscription().get(0).DIFF_TIME)
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
        initParseXML();
        parseXML.read();
        assertAll(
                () -> assertEquals("a14", parseXML.getTierTraduccion().get(0).ANNOTATION_ID),
                () -> assertEquals("ts2", parseXML.getTierTraduccion().get(0).TIME_SLOT_REF1),
                () -> assertEquals("34240", parseXML.getTierTraduccion().get(0).TIME_VALUE1),
                () -> assertEquals("ts6", parseXML.getTierTraduccion().get(0).TIME_SLOT_REF2),
                () -> assertEquals("39000", parseXML.getTierTraduccion().get(0).TIME_VALUE2),
                () -> assertEquals("El mal agüero se da incluso con el puro grito de las gallinas por la noche,", parseXML.getTierTraduccion().get(0).ANNOTATION_VALUE),
                () -> assertEquals(4760.0, parseXML.getTierTraduccion().get(0).DIFF_TIME)
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

        initParseXML();
        parseXML.read();
        assertAll(
                () -> assertEquals("a23", parseXML.getTierGlosado().get(0).ANNOTATION_ID),
                () -> assertEquals("ts3", parseXML.getTierGlosado().get(0).TIME_SLOT_REF1),
                () -> assertEquals("34240", parseXML.getTierGlosado().get(0).TIME_VALUE1),
                () -> assertEquals("ts7", parseXML.getTierGlosado().get(0).TIME_SLOT_REF2),
                () -> assertEquals("39000", parseXML.getTierGlosado().get(0).TIME_VALUE2),
                () -> assertEquals("DM mal.agüero=D2 hasta DM sólo ERG.3SG EP-grito DM gallina-PL con-ABS.3SG noche=D2", parseXML.getTierGlosado().get(0).ANNOTATION_VALUE),
                () -> assertEquals(4760.0, parseXML.getTierGlosado().get(0).DIFF_TIME)
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
        initParseXML();
        parseXML.read();
        assertEquals(morfemas.getClass().getSimpleName(), parseXML.getTierMorfemas().getClass().getSimpleName());
        assertAll(
                () -> assertEquals("a32", parseXML.getTierMorfemas().get(0).ANNOTATION_ID),
                () -> assertEquals("ts4", parseXML.getTierMorfemas().get(0).TIME_SLOT_REF1),
                () -> assertEquals("34240", parseXML.getTierMorfemas().get(0).TIME_VALUE1),
                () -> assertEquals("ts8", parseXML.getTierMorfemas().get(0).TIME_SLOT_REF2),
                () -> assertEquals("39000", parseXML.getTierMorfemas().get(0).TIME_VALUE2),
                () -> assertEquals("Le tomojchi’=o’, tak le chéen u y-awat le kaax-o’ob, yéetel-ø áak’ab=o’,", parseXML.getTierMorfemas().get(0).ANNOTATION_VALUE),
                () -> assertEquals(4760.0, parseXML.getTierMorfemas().get(0).DIFF_TIME)
        );
    }
}