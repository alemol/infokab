package mx.geoint.VideoCutter;

import mx.geoint.Controllers.VideoCutter.VideoCutter;
import org.junit.jupiter.api.Test;

class VideoCutterTest extends VideoCutter {
    VideoCutter cutter = new VideoCutter();
    @Test
    void cutter() {
        //BasicConfigurator.configure();
        //--> vectores de tiempos inicio y fin
        double s = 0;
        double  e = 10;
        cutter.Cutter("/home/centrogeo/JavaApps/infokab-backend/src/main/resources/2015-01-09_1650_Entrevista_datos_espontáneos_Clementina.MP4",34.240,38.94, "out");
    }

    @Test
    void concatenateVideoFromWriters() {
    }

}