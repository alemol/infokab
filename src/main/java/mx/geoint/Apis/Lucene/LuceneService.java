package mx.geoint.Apis.Lucene;

import mx.geoint.Controllers.ElanXmlDigester.ElanXmlDigester;
import mx.geoint.Controllers.Lucene.Lucene;
import mx.geoint.Database.DBProjects;
import mx.geoint.Model.Project.ProjectPostgresRegister;
import mx.geoint.pathSystem;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;

@Service
public class LuceneService {
    public static DBProjects dbProjects;

    public LuceneService(){
        this.dbProjects = new DBProjects();
    }

    public void indexLucene(String projectID, String indexName) throws SQLException, IOException, ParserConfigurationException, SAXException {
        ProjectPostgresRegister projectPostgresRegister = this.dbProjects.getProjectById(projectID);
        String uuid = projectPostgresRegister.getId_usuario();
        String pathEAF = projectPostgresRegister.getRuta_trabajo()+projectPostgresRegister.getNombre_proyecto()+".eaf";
        String projectName = projectPostgresRegister.getNombre_proyecto();
        String pathMultimedia = projectPostgresRegister.getRuta_trabajo()+projectPostgresRegister.getNombre_proyecto()+".wav";
        String pathAnnotations = "";

        ElanXmlDigester elanXmlDigester = new ElanXmlDigester(pathEAF, pathMultimedia, uuid, Integer.parseInt(projectID));

        if(indexName == "ALL"){
            pathAnnotations = projectPostgresRegister.getRuta_trabajo() + pathSystem.INDEX_LANGUAJE_MAYA+"/";
            elanXmlDigester.parse_tier(pathSystem.TIER_MAIN, true, true);
            Lucene lucene_maya = new Lucene(pathSystem.DIRECTORY_INDEX_GENERAL+"/"+pathSystem.INDEX_LANGUAJE_MAYA+"/"+projectName+"/");
            lucene_maya.initConfig(true);
            lucene_maya.createIndex(pathAnnotations);
            dbProjects.updateMayaIndex(Integer.parseInt(projectID), true);

            pathAnnotations = projectPostgresRegister.getRuta_trabajo() + pathSystem.INDEX_LANGUAJE_SPANISH+"/";
            elanXmlDigester.parse_tier(pathSystem.TIER_TRANSLATE, true, true);
            Lucene lucene_spanish = new Lucene(pathSystem.DIRECTORY_INDEX_GENERAL+"/"+pathSystem.INDEX_LANGUAJE_SPANISH+"/"+projectName+"/");
            lucene_spanish.initConfig(true);
            lucene_spanish.createIndex(pathAnnotations);
            dbProjects.updateSpanishIndex(Integer.parseInt(projectID), true);

            pathAnnotations = projectPostgresRegister.getRuta_trabajo() + pathSystem.INDEX_LANGUAJE_GLOSA+"/";
            elanXmlDigester.parse_tier(pathSystem.TIER_GlOSA_INDEX, true, true);

            Lucene lucene_glosa = new Lucene(pathSystem.DIRECTORY_INDEX_GENERAL+"/"+pathSystem.INDEX_LANGUAJE_GLOSA+"/"+projectName+"/");
            lucene_glosa.initConfig(true);
            lucene_glosa.createIndex(pathAnnotations);
            dbProjects.updateGlosaIndex(Integer.parseInt(projectID), true);

            pathAnnotations = projectPostgresRegister.getRuta_trabajo() + pathSystem.INDEX_LANGUAJE_GLOSA_WORDS+"/";
            elanXmlDigester.parse_tier(pathSystem.TIER_GlOSA_INDEX_WORDS, true, true);

            Lucene lucene_glosa_words = new Lucene(pathSystem.DIRECTORY_INDEX_GENERAL+"/"+pathSystem.INDEX_LANGUAJE_GLOSA_WORDS+"/"+projectName+"/");
            lucene_glosa_words.initConfig(true);
            lucene_glosa_words.createIndex(pathAnnotations);
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
            dbProjects.updateMayaIndex(Integer.parseInt(projectID), true);
        }

        if(indexName == pathSystem.INDEX_LANGUAJE_SPANISH){
            pathAnnotations = projectPostgresRegister.getRuta_trabajo() + pathSystem.INDEX_LANGUAJE_SPANISH+"/";
            elanXmlDigester.parse_tier(pathSystem.TIER_TRANSLATE, true, true);
            Lucene lucene_spanish = new Lucene(pathSystem.DIRECTORY_INDEX_GENERAL+"/"+pathSystem.INDEX_LANGUAJE_SPANISH+"/"+projectName+"/");
            lucene_spanish.initConfig(true);
            lucene_spanish.createIndex(pathAnnotations);
            dbProjects.updateSpanishIndex(Integer.parseInt(projectID), true);
        }

        if(indexName == pathSystem.INDEX_LANGUAJE_GLOSA){
            pathAnnotations = projectPostgresRegister.getRuta_trabajo() + pathSystem.INDEX_LANGUAJE_GLOSA+"/";
            elanXmlDigester.parse_tier(pathSystem.TIER_GlOSA_INDEX, true, true);
            Lucene lucene_glosa = new Lucene(pathSystem.DIRECTORY_INDEX_GENERAL+"/"+pathSystem.INDEX_LANGUAJE_GLOSA+"/"+projectName+"/");
            lucene_glosa.initConfig(true);
            lucene_glosa.createIndex(pathAnnotations);

            pathAnnotations = projectPostgresRegister.getRuta_trabajo() + pathSystem.INDEX_LANGUAJE_GLOSA_WORDS+"/";
            elanXmlDigester.parse_tier(pathSystem.TIER_GlOSA_INDEX_WORDS, true, true);
            Lucene lucene_glosa_words = new Lucene(pathSystem.DIRECTORY_INDEX_GENERAL+"/"+pathSystem.INDEX_LANGUAJE_GLOSA_WORDS+"/"+projectName+"/");
            lucene_glosa_words.initConfig(true);
            lucene_glosa_words.createIndex(pathAnnotations);
            dbProjects.updateGlosaIndex(Integer.parseInt(projectID), true);
        }
    }
}
