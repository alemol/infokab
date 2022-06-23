package mx.geoint.ElanXmlDigester;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ElanXmlDigesterTest {
    ElanXmlDigester elanXmlDigester;

    public void initElanXmlDigester(){
        elanXmlDigester = new ElanXmlDigester("src/main/resources/2015-01-09_1650_Entrevista_datos_espontáneos_Clementina.eaf");
        //elanXmlDigester = new ElanXmlDigester("src/main/resources/eligio_uikab_mena.eaf");
    }

    @Test
    void parse_tier() throws IOException {
        //{
        // "ANNOTATION_ID":"a5",
        // "TIME_SLOT_REF1":"ts1",
        // "TIME_VALUE1":"34240",
        // "TIME_SLOT_REF2":"ts5",
        // "TIME_VALUE2":"39000",
        // "ANNOTATION_VALUE":"Le tomojchi’o’, tak le chéen u yawat le kaaxo’ob, yéetel áak’abo’,",
        // "DIFF_TIME":4760.0
        // }
        initElanXmlDigester();
        elanXmlDigester.parse_tier("Oración", true, false);
    }

    @Test
    void getTierTraduccion() throws IOException {
        //{
        // "ANNOTATION_ID":"a14",
        // "TIME_SLOT_REF1":"ts2",
        // "TIME_VALUE1":"34240",
        // "TIME_SLOT_REF2":"ts6",
        // "TIME_VALUE2":"39000",
        // "ANNOTATION_VALUE":"El mal agüero se da incluso con el puro grito de las gallinas por la noche,",
        // "DIFF_TIME":4760.0
        // }
        initElanXmlDigester();
        elanXmlDigester.parse_tier("Traducción", false, false);
        assertAll(
                () -> assertEquals("a14", elanXmlDigester.getTier.get(0).ANNOTATION_ID),
                () -> assertEquals("ts2", elanXmlDigester.getTier.get(0).TIME_SLOT_REF1),
                () -> assertEquals("34240", elanXmlDigester.getTier.get(0).TIME_VALUE1),
                () -> assertEquals("ts6", elanXmlDigester.getTier.get(0).TIME_SLOT_REF2),
                () -> assertEquals("39000", elanXmlDigester.getTier.get(0).TIME_VALUE2),
                () -> assertEquals("El mal agüero se da incluso con el puro grito de las gallinas por la noche,", elanXmlDigester.getTier.get(0).ANNOTATION_VALUE),
                () -> assertEquals(4760.0, elanXmlDigester.getTier.get(0).DIFF_TIME)
        );
    }

    @Test
    void getTierGlosado() throws IOException {//{
        // "ANNOTATION_ID":"a23",
        // "TIME_SLOT_REF1":"ts3",
        // "TIME_VALUE1":"34240",
        // "TIME_SLOT_REF2":"ts7",
        // "TIME_VALUE2":"39000",
        // "ANNOTATION_VALUE":"DM mal.agüero=D2 hasta DM sólo ERG.3SG EP-grito DM gallina-PL con-ABS.3SG noche=D2",
        // "DIFF_TIME":4760.0
        // }

        initElanXmlDigester();
        elanXmlDigester.parse_tier("Glosado", false, false);
        assertAll(
                () -> assertEquals("a23", elanXmlDigester.getTier.get(0).ANNOTATION_ID),
                () -> assertEquals("ts3", elanXmlDigester.getTier.get(0).TIME_SLOT_REF1),
                () -> assertEquals("34240", elanXmlDigester.getTier.get(0).TIME_VALUE1),
                () -> assertEquals("ts7", elanXmlDigester.getTier.get(0).TIME_SLOT_REF2),
                () -> assertEquals("39000", elanXmlDigester.getTier.get(0).TIME_VALUE2),
                () -> assertEquals("DM mal.agüero=D2 hasta DM sólo ERG.3SG EP-grito DM gallina-PL con-ABS.3SG noche=D2", elanXmlDigester.getTier.get(0).ANNOTATION_VALUE),
                () -> assertEquals(4760.0, elanXmlDigester.getTier.get(0).DIFF_TIME)
        );
    }

    @Test
    void getTierMorfemas() throws IOException {
        // "ANNOTATION_ID":"a32",
        // "TIME_SLOT_REF1":"ts4",
        // "TIME_VALUE1":"34240",
        // "TIME_SLOT_REF2":"ts8",
        // "TIME_VALUE2":"39000",
        // "ANNOTATION_VALUE":"Le tomojchi’=o’, tak le chéen u y-awat le kaax-o’ob, yéetel-ø áak’ab=o’,",
        // "DIFF_TIME":4760.0
        // }
        initElanXmlDigester();
        elanXmlDigester.parse_tier("Morfemas", false, false);
        assertAll(
                () -> assertEquals("a32", elanXmlDigester.getTier.get(0).ANNOTATION_ID),
                () -> assertEquals("ts4", elanXmlDigester.getTier.get(0).TIME_SLOT_REF1),
                () -> assertEquals("34240", elanXmlDigester.getTier.get(0).TIME_VALUE1),
                () -> assertEquals("ts8", elanXmlDigester.getTier.get(0).TIME_SLOT_REF2),
                () -> assertEquals("39000", elanXmlDigester.getTier.get(0).TIME_VALUE2),
                () -> assertEquals("Le tomojchi’=o’, tak le chéen u y-awat le kaax-o’ob, yéetel-ø áak’ab=o’,", elanXmlDigester.getTier.get(0).ANNOTATION_VALUE),
                () -> assertEquals(4760.0, elanXmlDigester.getTier.get(0).DIFF_TIME)
        );
    }
}