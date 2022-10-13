package mx.geoint.ElanXmlDigester;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ElanXmlDigesterTest {
    ElanXmlDigester elanXmlDigester;

    public void initElanXmlDigesterVideo(){
        elanXmlDigester = new ElanXmlDigester("src/main/resources/2015-01-09_1650_Entrevista_datos_espontáneos_Clementina.eaf", "47eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
    }
    public void initElanXmlDigesterAudio(){
        elanXmlDigester = new ElanXmlDigester("src/main/resources/eligio_uikab_mena.eaf", "47eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
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
        initElanXmlDigesterAudio();
        elanXmlDigester.parse_tier("oracion", false, false);
        assertAll(
                () -> assertEquals("a5", elanXmlDigester.getTier.get(0).ANNOTATION_ID),
                () -> assertEquals("ts1", elanXmlDigester.getTier.get(0).TIME_SLOT_REF1),
                () -> assertEquals("34240", elanXmlDigester.getTier.get(0).TIME_VALUE1),
                () -> assertEquals("ts5", elanXmlDigester.getTier.get(0).TIME_SLOT_REF2),
                () -> assertEquals("39000", elanXmlDigester.getTier.get(0).TIME_VALUE2),
                () -> assertEquals("Le tomojchi’o’, tak le chéen u yawat le kaaxo’ob, yéetel áak’abo’,", elanXmlDigester.getTier.get(0).ANNOTATION_VALUE),
                () -> assertEquals(4760.0, elanXmlDigester.getTier.get(0).DIFF_TIME)
        );
    }
    @Test
    void getTierTraduccionAudio() throws ParserConfigurationException, SAXException, IOException {
        //{
        // "ANNOTATION_ID":"a14",
        // "TIME_SLOT_REF1":"ts2",
        // "TIME_VALUE1":"34240",
        // "TIME_SLOT_REF2":"ts6",
        // "TIME_VALUE2":"39000",
        // "ANNOTATION_VALUE":"El mal agüero se da incluso con el puro grito de las gallinas por la noche,",
        // "DIFF_TIME":4760.0
        // }
        initElanXmlDigesterAudio();
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
    void getTierGlosadoAudio() throws ParserConfigurationException, SAXException, IOException {//{
        // "ANNOTATION_ID":"a23",
        // "TIME_SLOT_REF1":"ts3",
        // "TIME_VALUE1":"34240",
        // "TIME_SLOT_REF2":"ts7",
        // "TIME_VALUE2":"39000",
        // "ANNOTATION_VALUE":"DM mal.agüero=D2 hasta DM sólo ERG.3SG EP-grito DM gallina-PL con-ABS.3SG noche=D2",
        // "DIFF_TIME":4760.0
        // }

        initElanXmlDigesterAudio();
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
    void getTierMorfemasAudio() throws ParserConfigurationException, SAXException, IOException {
        // "ANNOTATION_ID":"a32",
        // "TIME_SLOT_REF1":"ts4",
        // "TIME_VALUE1":"34240",
        // "TIME_SLOT_REF2":"ts8",
        // "TIME_VALUE2":"39000",
        // "ANNOTATION_VALUE":"Le tomojchi’=o’, tak le chéen u y-awat le kaax-o’ob, yéetel-ø áak’ab=o’,",
        // "DIFF_TIME":4760.0
        // }
        initElanXmlDigesterAudio();
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

    //SECTION TEST FOR VIDEO
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
        initElanXmlDigesterVideo();
        elanXmlDigester.parse_tier("Traducción", false, false);
        assertAll(
                () -> assertEquals("a13", elanXmlDigester.getTier.get(0).ANNOTATION_ID),
                () -> assertEquals("ts2", elanXmlDigester.getTier.get(0).TIME_SLOT_REF1),
                () -> assertEquals("21340", elanXmlDigester.getTier.get(0).TIME_VALUE1),
                () -> assertEquals("ts4", elanXmlDigester.getTier.get(0).TIME_SLOT_REF2),
                () -> assertEquals("22640", elanXmlDigester.getTier.get(0).TIME_VALUE2),
                () -> assertEquals("Trae el bejuco para que la pegue", elanXmlDigester.getTier.get(0).ANNOTATION_VALUE),
                () -> assertEquals(1300.0, elanXmlDigester.getTier.get(0).DIFF_TIME)
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

        initElanXmlDigesterVideo();
        elanXmlDigester.parse_tier("Glosado", false, false);
        assertAll(
                () -> assertEquals("a9", elanXmlDigester.getTier.get(0).ANNOTATION_ID),
                () -> assertEquals("ts2", elanXmlDigester.getTier.get(0).TIME_SLOT_REF1),
                () -> assertEquals("21340", elanXmlDigester.getTier.get(0).TIME_VALUE1),
                () -> assertEquals("ts4", elanXmlDigester.getTier.get(0).TIME_SLOT_REF2),
                () -> assertEquals("22640", elanXmlDigester.getTier.get(0).TIME_VALUE2),
                () -> assertEquals("Traer-DET bejuco-? HAB-1sg.A-pegar-? ", elanXmlDigester.getTier.get(0).ANNOTATION_VALUE),
                () -> assertEquals(1300.0, elanXmlDigester.getTier.get(0).DIFF_TIME)
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
        initElanXmlDigesterVideo();
        elanXmlDigester.parse_tier("Morfemas", false, false);
        assertAll(
                () -> assertEquals("a5", elanXmlDigester.getTier.get(0).ANNOTATION_ID),
                () -> assertEquals("ts2", elanXmlDigester.getTier.get(0).TIME_SLOT_REF1),
                () -> assertEquals("21340", elanXmlDigester.getTier.get(0).TIME_VALUE1),
                () -> assertEquals("ts4", elanXmlDigester.getTier.get(0).TIME_SLOT_REF2),
                () -> assertEquals("22640", elanXmlDigester.getTier.get(0).TIME_VALUE2),
                () -> assertEquals("Taas-e aak'-o' k-in-jats'ej", elanXmlDigester.getTier.get(0).ANNOTATION_VALUE),
                () -> assertEquals(1300.0, elanXmlDigester.getTier.get(0).DIFF_TIME)
        );
    }

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
        initElanXmlDigesterVideo();
        elanXmlDigester.parse_tier("oracion", false, false);
        assertAll(
                () -> assertEquals("a1", elanXmlDigester.getTier.get(0).ANNOTATION_ID),
                () -> assertEquals("ts2", elanXmlDigester.getTier.get(0).TIME_SLOT_REF1),
                () -> assertEquals("21340", elanXmlDigester.getTier.get(0).TIME_VALUE1),
                () -> assertEquals("ts4", elanXmlDigester.getTier.get(0).TIME_SLOT_REF2),
                () -> assertEquals("22640", elanXmlDigester.getTier.get(0).TIME_VALUE2),
                () -> assertEquals("Taase aak'o' kinjats'ej", elanXmlDigester.getTier.get(0).ANNOTATION_VALUE),
                () -> assertEquals(1300.0, elanXmlDigester.getTier.get(0).DIFF_TIME)
        );
    }
}