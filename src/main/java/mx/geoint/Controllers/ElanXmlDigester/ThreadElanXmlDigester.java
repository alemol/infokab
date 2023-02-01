package mx.geoint.Controllers.ElanXmlDigester;
import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Controllers.Lucene.Lucene;
import mx.geoint.Database.DBProjects;
import mx.geoint.pathSystem;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Queue;
import java.util.LinkedList;

public class ThreadElanXmlDigester extends Thread{
    Queue<ElanXmlDigester> elanXmlDigester = new LinkedList<>();
    Logger logger = new Logger();
    DBProjects dbProjects = new DBProjects();

    /**
     * Función que ejecuta el hilo cuando esta en activo
     */
    public void run(){
        while(true){
            System.out.println("RUN TREAHD: "+ elanXmlDigester.size() + " : " + Thread.currentThread().getName());
            if(elanXmlDigester.isEmpty()){
                deactivate();
            } else {
                process();
            }
        }
    }

    /**
     *
     * @param pathEAF String, ruta del archivo eaf
     * @param pathMultimedia String, ruta del archivo multimedia
     * @param uuid String, Identificador del usuario
     */
    public void add(String pathEAF, String pathMultimedia, String uuid, int projectID){
        elanXmlDigester.add(new ElanXmlDigester(pathEAF, pathMultimedia, uuid, projectID));
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
                //throw new RuntimeException(e);
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
    public void process(){
        Date startDate = new Date();
        ElanXmlDigester currentElanXmlDigester = elanXmlDigester.poll();
        int projectID = currentElanXmlDigester.projectID;

        try{
            currentElanXmlDigester.validateElanXmlDigester();
            currentElanXmlDigester.parse_tier(pathSystem.TIER_MAIN, true, true);
            currentElanXmlDigester.parse_tier(pathSystem.TIER_TRANSLATE, true, true);

            //String uuid = currentElanXmlDigester.getUUID();
            //Lucene lucene = new Lucene(pathSystem.DIRECTORY_INDEX_GENERAL+uuid+"/");
            //lucene.initConfig(false);
            //lucene.createIndex(currentElanXmlDigester.basePathJsonFiles());

            Date endDate = new Date();
            long difference_In_Time = endDate.getTime() - startDate.getTime();
            long difference_In_Seconds = (difference_In_Time / (1000)) % 60;
            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
            System.out.println("TIMER FINISHED THREAD: "+ difference_In_Seconds +"s " + difference_In_Minutes+"m");
        } catch (ParserConfigurationException e) {
            logger.appendToFile(e);
            //throw new RuntimeException(e);
        } catch (IOException e) {
            logger.appendToFile(e);
            //throw new RuntimeException(e);
        } catch (SAXException e) {
            logger.appendToFile(e);
            //throw new RuntimeException(e);
        } catch (SQLException e) {
            logger.appendToFile(e);
            //throw new RuntimeException(e);
        } finally {
            resetProcessProject(projectID);
        }
    }

    public void resetProcessProject(int projectID){
        try{
            dbProjects.updateProcess(projectID, false);
        } catch (SQLException e) {
            logger.appendToFile(e);
        }
    }
}
