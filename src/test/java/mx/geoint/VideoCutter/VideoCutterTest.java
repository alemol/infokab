package mx.geoint.VideoCutter;

import ch.qos.logback.classic.BasicConfigurator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VideoCutterTest extends mx.geoint.VideoCutter.VideoCutter {
    VideoCutter cutter = new VideoCutter();
    @Test
    void cutter() {
        //BasicConfigurator.configure();
        //--> vectores de tiempos inicio y fin
        double s = 0;
        double  e = 10;
        cutter.Cutter(34.240,38.94, "/home/centrogeo/JavaApps/infokab-backend/src/main/resources/2015-01-09_1650_Entrevista_datos_espontáneos_Clementina.MP4","out");
    }

    @Test
    void concatenateVideoFromWriters() {
    }

}