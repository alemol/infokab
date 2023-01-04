package mx.geoint.Database;

import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Model.Glosado.GlosaAnnotationsRequest;
import mx.geoint.Model.Glosado.GlosaStep;
import mx.geoint.Model.Project.ProjectPostgresRegister;
import mx.geoint.Controllers.ParseXML.ParseXML;
import org.apache.commons.io.FilenameUtils;
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
import java.util.UUID;

public class DBProjects {
    private Credentials credentials;
    private Logger logger;

    public DBProjects(){
        this.credentials = new Credentials();
        this.logger = new Logger();
    }

    public int createProject(String uuid, String basePath, String projectName, String date, String hablantes, String ubicacion, String radio, String circleBounds) throws SQLException {
        System.out.println("createProject");
        int id_project = 0;
        //---guardado a base de datos
        System.out.println("save to database: "+projectName);

        Connection conn = credentials.getConnection();
        System.out.println(conn);

        String SQL_INSERT = "INSERT INTO proyectos (id_usuario, nombre_proyecto, ruta_trabajo, fecha_creacion, fecha_archivo, hablantes, ubicacion, radio, bounds) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_proyecto";


        PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setObject(1, UUID.fromString(uuid));
        //preparedStatement.setObject();
        //preparedStatement.setString(1, uuid);
        preparedStatement.setString(2, projectName);
        preparedStatement.setString(3, basePath.toString());
        //preparedStatement.setString(4, contentType);
        preparedStatement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

        preparedStatement.setString(5, date);
        preparedStatement.setString(6, hablantes);
        preparedStatement.setString(7, ubicacion);
        preparedStatement.setInt(8, Integer.parseInt(radio));
        preparedStatement.setString(9, circleBounds);

        preparedStatement.execute();
        //int row = preparedStatement.executeUpdate();
        ResultSet rs = preparedStatement.getGeneratedKeys();
        if ( rs.next() ) {
            // Retrieve the auto generated key(s).
            id_project = rs.getInt(1);
            System.out.println("generated key");
            System.out.println(id_project);
        }

        conn.close();

        return id_project;
    }

    public ProjectPostgresRegister getProjectById(String id) throws  SQLException {
        String SQL_QUERY = "SELECT p.id_proyecto, p.nombre_proyecto, p.ruta_trabajo FROM proyectos as p WHERE p.id_proyecto = " + id;

        Connection conn = credentials.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);
        ResultSet rs = preparedStatement.executeQuery();

        ProjectPostgresRegister projectRegister = null;

        while(rs.next()) {
            projectRegister = new ProjectPostgresRegister();
            projectRegister.setId_proyecto(rs.getString(1));
            projectRegister.setNombre_proyecto(rs.getString(2));
            projectRegister.setRuta_trabajo(rs.getString(3));
        }
        rs.close();
        conn.close();
        return projectRegister;

    }

    public ArrayList<ProjectPostgresRegister> ListProjects() throws SQLException {
        ArrayList<ProjectPostgresRegister> result = new ArrayList<>();
        ProjectPostgresRegister projectRegistrations = null;
        String SQL_QUERY =  "select p.id_proyecto, p.id_usuario, p.nombre_proyecto, p.ruta_trabajo, p.fecha_creacion, p.estado, p.fecha_archivo, p.hablantes, p.ubicacion, p.radio, p.bounds, p.total_de_anotaciones, count(distinct r.id) as total_de_reportes, count(distinct g.id) as total_de_anotaciones \n" +
                            "FROM proyectos as p \n" +
                            "left join reportes as r on r.id_proyecto = p.id_proyecto and r.activate=true\n" +
                            "left join glosado as g on g.proyecto_id = p.id_proyecto\n" +
                            "group by p.id_proyecto \n" +
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

            projectRegistrations = new ProjectPostgresRegister();
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
            projectRegistrations.setTotal_de_anotaciones(rs.getInt(12));
            projectRegistrations.setTotal_de_reportes(rs.getInt(13));
            projectRegistrations.setAnotaciones_guardadas(rs.getInt(14));


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