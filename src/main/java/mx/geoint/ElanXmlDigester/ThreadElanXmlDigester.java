package mx.geoint.ElanXmlDigester;
import mx.geoint.Logger.Logger;
import mx.geoint.Lucene.Lucene;
import mx.geoint.pathSystem;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Date;
import java.util.Queue;
import java.util.LinkedList;

public class ThreadElanXmlDigester extends Thread{
    Queue<ElanXmlDigester> elanXmlDigester = new LinkedList<>();
    Logger logger = new Logger();
    /**
     * Función que ejecuta el hilo cuando esta en activo
     */
    public void run(){
        while(true){
            System.out.println("RUN TREAHD: "+ elanXmlDigester.size() + " : " + Thread.currentThread().getName());
            if(elanXmlDigester.isEmpty()){
                deactivate();
            } else {
                try {
                    process();
                } catch (ParserConfigurationException e) {
                    Logger.appendToFile(e);
                    throw new RuntimeException(e);
                } catch (SAXException e) {
                    Logger.appendToFile(e);
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    Logger.appendToFile(e);
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     *
     * @param pathEAF String, ruta del archivo eaf
     * @param pathMultimedia String, ruta del archivo multimedia
     * @param uuid String, Identificador del usuario
     */
    public void add(String pathEAF, String pathMultimedia, String uuid){
        elanXmlDigester.add(new ElanXmlDigester(pathEAF, pathMultimedia, uuid));
    }

    /**
     * Función para desactivar el hilo activo
     */
    public void deactivate(){
        synchronized (this) {
            try {
                System.out.println("Thread deactivate");
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Funcion para activar el hilo inactivo
     */
    public void activate(){
        synchronized (this) {
            System.out.println("Thread activate");
            this.notify();
        }
    }

    /**
     * Función para obtener un elemento de la queue y ejecutar su proceso
     */
    public void process() throws ParserConfigurationException, SAXException, IOException{
        try {
            Date startDate = new Date();
            ElanXmlDigester currentElanXmlDigester = elanXmlDigester.poll();
            currentElanXmlDigester.parse_tier("oracion", true, true);

            String uuid = currentElanXmlDigester.getUUID();
            Lucene lucene = new Lucene(pathSystem.DIRECTORY_INDEX_GENERAL+uuid+"/");
            lucene.initConfig(false);
            lucene.createIndex(currentElanXmlDigester.basePathJsonFiles());

            Date endDate = new Date();
            long difference_In_Time = endDate.getTime() - startDate.getTime();
            long difference_In_Seconds = (difference_In_Time / (1000)) % 60;
            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
            System.out.println("TIMER FINISHED THREAD: "+ difference_In_Seconds +"s " + difference_In_Minutes+"m");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
