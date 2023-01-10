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

    public void indexLucene(String projectID) throws SQLException, IOException, ParserConfigurationException, SAXException {
        ProjectPostgresRegister projectPostgresRegister = this.dbProjects.getProjectById(projectID);
        String uuid = projectPostgresRegister.getId_usuario();
        String pathEAF = projectPostgresRegister.getRuta_trabajo()+projectPostgresRegister.getNombre_proyecto()+".eaf";
        String pathMultimedia = projectPostgresRegister.getRuta_trabajo()+projectPostgresRegister.getNombre_proyecto()+".wav";

        ElanXmlDigester elanXmlDigester = new ElanXmlDigester(pathEAF, pathMultimedia, uuid, Integer.parseInt(projectID));
        elanXmlDigester.parse_tier(pathSystem.TIER_MAIN, true, true);

        Lucene lucene = new Lucene(pathSystem.DIRECTORY_INDEX_GENERAL+uuid+"/");
        lucene.initConfig(false);
        lucene.createIndex(elanXmlDigester.basePathJsonFiles());
    }
}
