package mx.geoint.ElanXmlDigester;

import mx.geoint.Controllers.Logger.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class ThreadValidateElanXmlDigester extends Thread{
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
                validateProcess();
            }
        }
    }

    /**
     *
     * @param pathEAF String, ruta del archivo eaf
     * @param uuid String, Identificador del usuario
     */
    public void add(String pathEAF, String uuid, int projectID){
        elanXmlDigester.add(new ElanXmlDigester(pathEAF, null, uuid, projectID));
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
                logger.appendToFile(e);
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
     * Proceso para validar un archivo eaf
     */
    public void validateProcess(){
        try{
            Date startDate = new Date();
            ElanXmlDigester currentElanXmlDigester = elanXmlDigester.poll();
            currentElanXmlDigester.validateElanXmlDigester();

            Date endDate = new Date();
            long difference_In_Time = endDate.getTime() - startDate.getTime();
            long difference_In_Seconds = (difference_In_Time / (1000)) % 60;
            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
            System.out.println("TIMER FINISHED THREAD: "+ difference_In_Seconds +"s " + difference_In_Minutes+"m");
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            logger.appendToFile(e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.appendToFile(e);
            throw new RuntimeException(e);
        } catch (SAXException e) {
            logger.appendToFile(e);
            throw new RuntimeException(e);
        }
    }
}
