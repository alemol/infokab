package mx.geoint.database;

import mx.geoint.Glosa.Dictionary.DictionaryRequest;
import mx.geoint.Images.GalleryModel;
import mx.geoint.Logger.Logger;
import mx.geoint.Model.GlosaAnnotationsRequest;
import mx.geoint.Model.GlosaStep;
import mx.geoint.Model.ProjectRegistration;
import mx.geoint.Model.UsersList;
import mx.geoint.ParseXML.ParseXML;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DBProjects {
    private Credentials credentials;
    private Logger logger;

    public DBProjects(){
        this.credentials = new Credentials();
        this.logger = new Logger();
    }

    public ProjectRegistration getProjectById(String id) throws  SQLException {
        String SQL_QUERY = "SELECT p.id_proyecto, p.nombre_proyecto, p.ruta_trabajo FROM proyectos as p WHERE p.id_proyecto = " + id;

        Connection conn = credentials.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);
        ResultSet rs = preparedStatement.executeQuery();

        ProjectRegistration projectRegister = null;

        while(rs.next()) {
            projectRegister = new ProjectRegistration();
            projectRegister.setId_proyecto(rs.getString(1));
            projectRegister.setNombre_proyecto(rs.getString(2));
            projectRegister.setRuta_trabajo(rs.getString(3));
        }
        rs.close();
        conn.close();
        return projectRegister;

    }

    public ArrayList<ProjectRegistration> ListProjects() throws SQLException {
        ArrayList<ProjectRegistration> result = new ArrayList<>();
        ProjectRegistration projectRegistrations = null;
        String SQL_QUERY = "select\n" +
                            "p.*, count(r.id_proyecto) as total_de_reportes\n" +
                            "FROM proyectos as p\n" +
                            "left join reportes as r on r.id_proyecto = p.id_proyecto and r.activate=true\n" +
                            "group by p.id_proyecto\n" +
                            "order by p.id_proyecto";

        Connection conn = credentials.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            int random_total = rs.getInt(12);
            int random_save = 0;

            if(random_total > 0){
                random_save = (int)Math.floor(Math.random()*(random_total-1+1)+1);
            }

            projectRegistrations = new ProjectRegistration();
            projectRegistrations.setId_proyecto(rs.getString(1));
            projectRegistrations.setId_usuario(rs.getString(2));
            projectRegistrations.setNombre_proyecto(rs.getString(3));
            projectRegistrations.setRuta_trabajo(rs.getString(4));
            projectRegistrations.setFecha_creacion(rs.getString(5));
            projectRegistrations.setEstado(rs.getString(6));
            projectRegistrations.setFecha_archivo(rs.getString(7));
            projectRegistrations.setHablantes(rs.getString(8));
            projectRegistrations.setUbicacion(rs.getString(9));
            projectRegistrations.setRadio(rs.getInt(10));
            projectRegistrations.setBounds(rs.getString(11));
            projectRegistrations.setTotal_de_reportes(rs.getInt(14));

            projectRegistrations.setTotal_de_anotaciones(random_total);
            projectRegistrations.setAnotaciones_guardadas(random_save);

            String dir = rs.getString(4) + "/Images/";
            if (Files.exists(Path.of(dir))){
                int lastIndex = 0;
                String[] pathnames;

                File f = new File(dir);

                pathnames = f.list();
                if(pathnames.length>0){
                    for (String pathname : pathnames) {
                        String x = FilenameUtils.getBaseName(pathname);
                        lastIndex = Integer.parseInt(x.split("image")[1]);
                    }
                    projectRegistrations.setimageList(pathnames);
                    projectRegistrations.setLastIndex(lastIndex);
                }else{
                    projectRegistrations.setimageList(null);
                    projectRegistrations.setLastIndex(lastIndex);
                }
            }


            result.add(projectRegistrations);
        }

        rs.close();
        conn.close();
        return result;
    }

    public boolean setProjectAnnotationsCounter(Integer id_project, Integer count) throws SQLException {
        Connection conn = credentials.getConnection();
        String SQL_UPDATE = "UPDATE proyectos SET total_de_anotaciones = ? WHERE id_proyecto=?";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE);
        preparedStatement.setInt(1, count);
        preparedStatement.setObject(2, id_project);
        int rs = preparedStatement.executeUpdate();
        conn.close();

        if(rs>0){
            System.out.println("registro actualizado en base de datos");
            return true;
        } else{
            System.out.println("No se pudo actualizar el registro en base de datos");
            return false;
        }
    }

    public int getGlossingAnnotationInEaf(int projectId) throws SQLException {
        Connection conn = credentials.getConnection();
        String SQL_UPDATE = "SELECT anotaciones_guardadas FROM proyectos WHERE id_proyecto=?";
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE);
        preparedStatement.setInt(1, projectId);
        ResultSet row = preparedStatement.executeQuery();

        int total_de_anotaciones = 0;
        while(row.next()){
            total_de_anotaciones = row.getInt(1);
        }

        row.close();
        preparedStatement.close();
        conn.close();

        return total_de_anotaciones;
    }

    public boolean setGlossingAnnotationToEaf(Integer id_project, Integer count, GlosaAnnotationsRequest glosaAnnotationsRequest) throws SQLException{
        String projectName = glosaAnnotationsRequest.getFilePath();
        String annotationId = glosaAnnotationsRequest.getAnnotationID();
        String annotationREF = "";
        if(glosaAnnotationsRequest.getAnnotationREF().isEmpty()){
            annotationREF = glosaAnnotationsRequest.getAnnotationID();
        }else{
            annotationREF = glosaAnnotationsRequest.getAnnotationREF();
        }

        ArrayList<GlosaStep> steps = glosaAnnotationsRequest.getSteps();
        boolean answer = false;

        Connection conn = credentials.getConnection();
        conn.setAutoCommit(false);
        String SQL_UPDATE = "UPDATE proyectos SET anotaciones_guardadas = ? WHERE id_proyecto=?";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE);
        preparedStatement.setInt(1, count);
        preparedStatement.setObject(2, id_project);
        int rs = preparedStatement.executeUpdate();
        try{
            ParseXML parseXML = new ParseXML(projectName, "Glosado");
            parseXML.writeElement(annotationREF, annotationId, steps);

            conn.commit();
            if(rs>0){
                System.out.println("registro actualizado en base de datos");
                answer = true;
            } else{
                System.out.println("No se pudo actualizar el registro en base de datos");
                answer = false;
            }

        } catch (ParserConfigurationException e) {
            conn.rollback();
            answer = false;
            logger.appendToFile(e);
        } catch (IOException e) {
            conn.rollback();
            answer = false;
            logger.appendToFile(e);
        } catch (TransformerException e) {
            conn.rollback();
            answer = false;
            logger.appendToFile(e);
        } catch (SAXException e) {
            conn.rollback();
            answer = false;
            logger.appendToFile(e);
        }

        preparedStatement.close();
        conn.close();
        return answer;
    }
    public boolean updateDateEaf(Integer id_project, Integer count) throws SQLException {
        Connection conn = credentials.getConnection();
        String SQL_UPDATE = "UPDATE proyectos SET fecha_eaf = ? WHERE id_proyecto=?";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE);
        preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
        preparedStatement.setObject(2, id_project);

        int rs = preparedStatement.executeUpdate();
        conn.close();

        if(rs>0){
            System.out.println("registro actualizado en base de datos");
            return true;
        } else{
            System.out.println("No se pudo actualizar el registro en base de datos");
            return false;
        }
    }

    public boolean updateDateMultimedia(Integer id_project, Integer count) throws SQLException {
        Connection conn = credentials.getConnection();
        String SQL_UPDATE = "UPDATE proyectos SET fecha_multimedia = ? WHERE id_proyecto=?";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE);
        preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
        preparedStatement.setObject(2, id_project);

        int rs = preparedStatement.executeUpdate();
        conn.close();

        if(rs>0){
            System.out.println("registro actualizado en base de datos");
            return true;
        } else{
            System.out.println("No se pudo actualizar el registro en base de datos");
            return false;
        }
    }
}
