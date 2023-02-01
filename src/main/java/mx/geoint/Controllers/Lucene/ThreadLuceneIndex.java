package mx.geoint.Controllers.Lucene;

import mx.geoint.Controllers.ElanXmlDigester.ElanXmlDigester;
import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Database.DBProjects;
import mx.geoint.Model.Lucene.LuceneProjectRequest;
import mx.geoint.Model.Project.ProjectPostgresRegister;
import mx.geoint.pathSystem;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class ThreadLuceneIndex extends Thread {
    Queue<LuceneProjectRequest> luceneIndex = new LinkedList<>();
    Logger logger = new Logger();
    DBProjects dbProjects = new DBProjects();

    /**
     * Función que ejecuta el hilo cuando esta en activo
     */
    public void run(){
        while(true){
            System.out.println("RUN TREAHD: "+ luceneIndex.size() + " : " + Thread.currentThread().getName());
            if(luceneIndex.isEmpty()){
                deactivate();
            } else {
                process();
            }
        }
    }


    public void add(LuceneProjectRequest luceneProjectRequest){
        luceneIndex.add(luceneProjectRequest);
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
        LuceneProjectRequest luceneProjectRequest = luceneIndex.poll();

        boolean maya = luceneProjectRequest.getMaya();
        boolean spanish = luceneProjectRequest.getSpanish();
        boolean glosa = luceneProjectRequest.getGlosa();
        int projectID = Integer.parseInt(luceneProjectRequest.getProjectID());

        try{
            if(maya){
                indexProjectLucene(luceneProjectRequest.getProjectID(),pathSystem.INDEX_LANGUAJE_MAYA);
                dbProjects.updateMayaIndex(projectID, true);
            }
            if(spanish){
                indexProjectLucene(luceneProjectRequest.getProjectID(),pathSystem.INDEX_LANGUAJE_SPANISH);
                dbProjects.updateSpanishIndex(projectID, true);
            }
            if(glosa){
                indexProjectLucene(luceneProjectRequest.getProjectID(), pathSystem.INDEX_LANGUAJE_GLOSA);
                dbProjects.updateGlosaIndex(projectID, true);
            }

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

    public void indexProjectLucene(String projectID, String indexName) throws SQLException, IOException, ParserConfigurationException, SAXException {
        ProjectPostgresRegister projectPostgresRegister = this.dbProjects.getProjectById(projectID);
        String uuid = projectPostgresRegister.getId_usuario();
        String pathEAF = projectPostgresRegister.getRuta_trabajo()+projectPostgresRegister.getNombre_proyecto()+".eaf";
        String projectName = projectPostgresRegister.getNombre_proyecto();
        String pathMultimedia = projectPostgresRegister.getRuta_trabajo()+projectPostgresRegister.getNombre_proyecto()+".wav";
        String pathAnnotations = "";

        ElanXmlDigester elanXmlDigester = new ElanXmlDigester(pathEAF, pathMultimedia, uuid, Integer.parseInt(projectID));

        if(indexName == pathSystem.INDEX_LANGUAJE_MAYA){
            pathAnnotations = projectPostgresRegister.getRuta_trabajo() + pathSystem.INDEX_LANGUAJE_MAYA+"/";
            elanXmlDigester.parse_tier(pathSystem.TIER_MAIN, true, true);
            Lucene lucene_maya = new Lucene(pathSystem.DIRECTORY_INDEX_GENERAL+"/"+pathSystem.INDEX_LANGUAJE_MAYA+"/"+projectName+"/");
            lucene_maya.initConfig(true);
            lucene_maya.createIndex(pathAnnotations);
        }

        if(indexName == pathSystem.INDEX_LANGUAJE_SPANISH){
            pathAnnotations = projectPostgresRegister.getRuta_trabajo() + pathSystem.INDEX_LANGUAJE_SPANISH+"/";
            elanXmlDigester.parse_tier(pathSystem.TIER_TRANSLATE, true, true);
            Lucene lucene_spanish = new Lucene(pathSystem.DIRECTORY_INDEX_GENERAL+"/"+pathSystem.INDEX_LANGUAJE_SPANISH+"/"+projectName+"/");
            lucene_spanish.initConfig(true);
            lucene_spanish.createIndex(pathAnnotations);
        }

        if(indexName == pathSystem.INDEX_LANGUAJE_GLOSA){
            pathAnnotations = projectPostgresRegister.getRuta_trabajo() + pathSystem.INDEX_LANGUAJE_GLOSA+"/";
            elanXmlDigester.parse_tier(pathSystem.TIER_GlOSA_INDEX, true, true);
            Lucene lucene_glosa = new Lucene(pathSystem.DIRECTORY_INDEX_GENERAL+"/"+pathSystem.INDEX_LANGUAJE_GLOSA+"/"+projectName+"/");
            lucene_glosa.initConfig(true);
            lucene_glosa.createIndex(pathAnnotations);
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
