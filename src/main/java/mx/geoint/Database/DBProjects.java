package mx.geoint.Database;

import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Controllers.WriteXML.WriteXML;
import mx.geoint.Model.Annotation.AnnotationsRequest;
import mx.geoint.Model.Glosado.GlosaStep;
import mx.geoint.Model.Project.ProjectPostgresLocations;
import mx.geoint.Model.Project.ProjectPostgresRegister;
import mx.geoint.Controllers.ParseXML.ParseXML;
import mx.geoint.pathSystem;
import org.apache.commons.io.FileUtils;
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
        String[] parts = projectName.split("_");
        String[] coords = ubicacion.split(",");
        Connection conn = credentials.getConnection();
        System.out.println(conn);

        String SQL_INSERT = "INSERT INTO proyectos (id_usuario, nombre_proyecto, ruta_trabajo, fecha_creacion, fecha_archivo, hablantes, ubicacion, radio, bounds, en_proceso, indice_maya, indice_español, indice_glosado, entidad, municipio, localidad, cvegeo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,(SELECT entidad_nombre FROM public.dim_entidad WHERE entidad_cvegeo = ?),(SELECT municipio_nombre FROM public.dim_municipio WHERE municipio_cvegeo = ? limit 1),(SELECT l.localidad_nombre FROM (SELECT localidad_nombre, geom <-> ST_SetSRID(ST_MakePoint(?::float,?::float),4326) AS dist FROM public.dim_localidad_rural WHERE municipio_cvegeo = ? UNION SELECT localidad_nombre, geom <-> ST_SetSRID(ST_MakePoint(?::float,?::float),4326) AS dist FROM public.dim_localidad_rural WHERE municipio_cvegeo = ?  ORDER BY dist LIMIT 1) AS l), (SELECT l.localidad_cvegeo FROM (SELECT localidad_cvegeo, geom <-> ST_SetSRID(ST_MakePoint(?::float,?::float),4326) AS dist FROM public.dim_localidad_rural WHERE municipio_cvegeo = ? UNION SELECT localidad_cvegeo, geom <-> ST_SetSRID(ST_MakePoint(?::float,?::float),4326) AS dist FROM public.dim_localidad_rural WHERE municipio_cvegeo = ?  ORDER BY dist LIMIT 1) AS l) ) RETURNING id_proyecto";


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
        preparedStatement.setBoolean(10, false);
        preparedStatement.setBoolean(11, false);
        preparedStatement.setBoolean(12, false);
        preparedStatement.setBoolean(13, false);
        preparedStatement.setString(14, parts[0]);
        preparedStatement.setString(15, parts[0]+parts[1]);
        preparedStatement.setString(16, coords[1]);
        preparedStatement.setString(17, coords[0]);
        preparedStatement.setString(18, parts[0]+parts[1]);
        preparedStatement.setString(19, coords[1]);
        preparedStatement.setString(20, coords[0]);
        preparedStatement.setString(21, parts[0]+parts[1]);
        preparedStatement.setString(22, coords[1]);
        preparedStatement.setString(23, coords[0]);
        preparedStatement.setString(24, parts[0]+parts[1]);
        preparedStatement.setString(25, coords[1]);
        preparedStatement.setString(26, coords[0]);
        preparedStatement.setString(27, parts[0]+parts[1]);


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
        String SQL_QUERY = "SELECT p.id_proyecto, p.nombre_proyecto, p.ruta_trabajo, p.id_usuario, p.cvegeo FROM proyectos as p WHERE p.id_proyecto=?";

        Connection conn = credentials.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);
        preparedStatement.setInt(1, Integer.parseInt(id));
        ResultSet rs = preparedStatement.executeQuery();

        ProjectPostgresRegister projectRegister = null;

        while(rs.next()) {
            projectRegister = new ProjectPostgresRegister();
            projectRegister.setId_proyecto(rs.getString(1));
            projectRegister.setNombre_proyecto(rs.getString(2));
            projectRegister.setRuta_trabajo(rs.getString(3));
            projectRegister.setId_usuario(rs.getString(4));
            projectRegister.setCvegeo(rs.getString(5));
        }
        rs.close();
        conn.close();
        return projectRegister;

    }

    public String[] getProjectByName(String filename) throws  SQLException {

        String[] parts = filename.split("_");
        String SQL_QUERY = "SELECT p.fecha_archivo, p.hablantes, p.entidad, p.municipio, p.localidad, p.ubicacion, ST_Expand(BOX2D(l.geom),0.005) as bbox\n" +
                "FROM proyectos p ,\n" +
                "(\n" +
                "\tSELECT localidad_nombre,municipio_cvegeo, geom \n" +
                "\tFROM public.dim_localidad_rural \n" +
                "\tUNION\n" +
                "\tSELECT localidad_nombre,municipio_cvegeo, geom \n" +
                "\tFROM public.dim_localidad_urbana \n" +
                ") AS l \n" +
                "WHERE p.nombre_proyecto =? \n" +
                "AND l.localidad_nombre = p.localidad \n"+
                "AND l.municipio_cvegeo = ?";

        Connection conn = credentials.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);
        preparedStatement.setString(1, filename);
        preparedStatement.setString(2, parts[0]+parts[1]);
        ResultSet rs = preparedStatement.executeQuery();
        String[] resultados = new String[7];
        while(rs.next()) {
            resultados[0] = rs.getString(1);
            resultados[1] = rs.getString(2);
            resultados[2] = rs.getString(3);
            resultados[3] = rs.getString(4);
            resultados[4] = rs.getString(5);
            resultados[5] = rs.getString(6);
            resultados[6] = rs.getString(7);
        }
        rs.close();
        conn.close();
        return resultados;
    }

    public ArrayList<ProjectPostgresRegister> ListProjects() throws SQLException {
        ArrayList<ProjectPostgresRegister> result = new ArrayList<>();
        ProjectPostgresRegister projectRegistrations = null;
        String SQL_QUERY =  "select p.id_proyecto, p.id_usuario, p.nombre_proyecto, p.ruta_trabajo, p.fecha_creacion, p.estado, p.fecha_archivo, p.hablantes, p.ubicacion, p.radio, p.bounds, p.total_de_anotaciones, p.en_proceso, p.indice_maya, p.indice_español, p.indice_glosado," +
                            " count(distinct r.id) as total_de_reportes, count(distinct g.id) as total_de_anotaciones \n" +
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
            projectRegistrations.setEn_proceso(rs.getBoolean(13));
            projectRegistrations.setIndex_maya(rs.getBoolean(14));
            projectRegistrations.setIndex_español(rs.getBoolean(15));
            projectRegistrations.setIndex_glosado(rs.getBoolean(16));

            projectRegistrations.setTotal_de_reportes(rs.getInt(17));
            projectRegistrations.setAnotaciones_guardadas(rs.getInt(18));


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

    public boolean setGlossingAnnotationToEaf(Integer id_project, Integer count, AnnotationsRequest annotationsRequest) throws SQLException{
        String projectName = annotationsRequest.getFilePath();
        String annotationId = annotationsRequest.getAnnotationID();
        String annotationREF = "";
        if(annotationsRequest.getAnnotationREF().isEmpty()){
            annotationREF = annotationsRequest.getAnnotationID();
        }else{
            annotationREF = annotationsRequest.getAnnotationREF();
        }

        ArrayList<GlosaStep> steps = annotationsRequest.getSteps();
        boolean answer = false;

        Connection conn = credentials.getConnection();
        conn.setAutoCommit(false);
        String SQL_UPDATE = "UPDATE proyectos SET anotaciones_guardadas = ? WHERE id_proyecto=?";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE);
        preparedStatement.setInt(1, count);
        preparedStatement.setObject(2, id_project);
        int rs = preparedStatement.executeUpdate();
        try{
            WriteXML writeXML = new WriteXML(projectName);
            writeXML.writeElement(annotationREF, annotationId, steps);

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

    public boolean deleteProject(int projectID, String projectName) throws SQLException {
        boolean result = false;

        Connection conn = credentials.getConnection();
        conn.setAutoCommit(false);

        String SQL_QUERY_GLOSADO = "DELETE FROM glosado WHERE proyecto_id=?";
        PreparedStatement preparedStatement_glosa = conn.prepareStatement(SQL_QUERY_GLOSADO);
        preparedStatement_glosa.setInt(1, projectID);

        String SQL_QUERY_REPORTS = "DELETE FROM reportes WHERE id_proyecto=?";
        PreparedStatement preparedStatement_report = conn.prepareStatement(SQL_QUERY_REPORTS);
        preparedStatement_report.setInt(1, projectID);

        String SQL_QUERY_GET_PROJECT = "SELECT ruta_trabajo FROM proyectos WHERE id_proyecto=?";
        PreparedStatement preparedStatement_get_project = conn.prepareStatement(SQL_QUERY_GET_PROJECT);
        preparedStatement_get_project.setInt(1, projectID);

        String SQL_QUERY_PROJECTS = "DELETE FROM proyectos WHERE id_proyecto=?";
        PreparedStatement preparedStatement_project = conn.prepareStatement(SQL_QUERY_PROJECTS);
        preparedStatement_project.setInt(1, projectID);

        try{
            ResultSet rs = preparedStatement_get_project.executeQuery();
            while(rs.next()) {
                File dir_work = new File(rs.getString(1));
                FileUtils.deleteDirectory(dir_work.getCanonicalFile());
            }

            File dir_maya = new File(pathSystem.DIRECTORY_INDEX_GENERAL+pathSystem.INDEX_LANGUAJE_MAYA+"/"+projectName+"/");
            FileUtils.deleteDirectory(dir_maya.getCanonicalFile());

            File dir_español = new File(pathSystem.DIRECTORY_INDEX_GENERAL+pathSystem.INDEX_LANGUAJE_SPANISH+"/"+projectName+"/");
            FileUtils.deleteDirectory(dir_español.getCanonicalFile());

            File dir_glosa = new File(pathSystem.DIRECTORY_INDEX_GENERAL+pathSystem.INDEX_LANGUAJE_GLOSA+"/"+projectName+"/");
            FileUtils.deleteDirectory(dir_glosa.getCanonicalFile());

            preparedStatement_glosa.executeUpdate();
            preparedStatement_report.executeUpdate();
            preparedStatement_project.executeUpdate();
            conn.commit();
            result = true;
        }catch (SQLException e){
            logger.appendToFile(e);
            conn.rollback();
            result = false;
        } catch (IOException e) {
            logger.appendToFile(e);
            conn.rollback();
            result = false;
        } finally {
            conn.close();
        }

        conn.close();
        return result;
    }

    public boolean updateProcess(Integer id_project, boolean process) throws SQLException {
        Connection conn = credentials.getConnection();
        String SQL_UPDATE = "UPDATE proyectos SET en_proceso = ? WHERE id_proyecto=?";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE);
        preparedStatement.setBoolean(1, process);
        preparedStatement.setObject(2, id_project);

        int rs = preparedStatement.executeUpdate();
        boolean result;

        if(rs>0){
            System.out.println("registro actualizado en base de datos");
            result = true;
        } else{
            System.out.println("No se pudo actualizar el registro en base de datos");
            result = false;
        }

        return result;
    }

    public boolean updateMayaIndex(Integer id_project, boolean status) throws SQLException {
        Connection conn = credentials.getConnection();
        String SQL_UPDATE = "UPDATE proyectos SET indice_maya = ? WHERE id_proyecto=?";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE);
        preparedStatement.setBoolean(1, status);
        preparedStatement.setObject(2, id_project);

        int rs = preparedStatement.executeUpdate();
        conn.close();
        boolean result;

        if(rs>0){
            System.out.println("registro actualizado en base de datos");
            result = true;
        } else{
            System.out.println("No se pudo actualizar el registro en base de datos");
            result = false;
        }

        return result;
    }

    public boolean updateSpanishIndex(Integer id_project, boolean status) throws SQLException {
        Connection conn = credentials.getConnection();
        String SQL_UPDATE = "UPDATE proyectos SET indice_español = ? WHERE id_proyecto=?";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE);
        preparedStatement.setBoolean(1, status);
        preparedStatement.setObject(2, id_project);

        int rs = preparedStatement.executeUpdate();
        conn.close();
        boolean result;

        if(rs>0){
            System.out.println("registro actualizado en base de datos");
            result = true;
        } else{
            System.out.println("No se pudo actualizar el registro en base de datos");
            result = false;
        }

        return result;
    }

    public boolean updateGlosaIndex(Integer id_project, boolean status) throws SQLException {
        Connection conn = credentials.getConnection();
        String SQL_UPDATE = "UPDATE proyectos SET indice_glosado = ? WHERE id_proyecto=?";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE);
        preparedStatement.setBoolean(1, status);
        preparedStatement.setObject(2, id_project);

        int rs = preparedStatement.executeUpdate();
        conn.close();
        boolean result;

        if(rs>0){
            System.out.println("registro actualizado en base de datos");
            result = true;
        } else{
            System.out.println("No se pudo actualizar el registro en base de datos");
            result = false;
        }

        return result;
    }

    public boolean resetFlagIndex(Integer id_project, boolean status) throws SQLException {
        Connection conn = credentials.getConnection();
        String SQL_UPDATE = "UPDATE proyectos SET indice_glosado = ?, indice_español = ?, indice_maya = ? WHERE id_proyecto=?";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE);
        preparedStatement.setBoolean(1, status);
        preparedStatement.setBoolean(2, status);
        preparedStatement.setBoolean(3, status);
        preparedStatement.setObject(4, id_project);

        int rs = preparedStatement.executeUpdate();
        conn.close();
        boolean result;

        if(rs>0){
            System.out.println("registro actualizado en base de datos");
            result = true;
        } else{
            System.out.println("No se pudo actualizar el registro en base de datos");
            result = false;
        }

        return result;
    }


    public ArrayList<ProjectPostgresLocations> getLocations(String[] cvegeo) throws  SQLException {
        ArrayList<ProjectPostgresLocations> result = new ArrayList<>();
        String SQL_QUERY = "SELECT l.localidad_cvegeo, l.localidad_nombre, l.municipio_cvegeo, ST_Expand(BOX2D(l.geom),0.005) as bbox\n" +
                "FROM \n" +
                "(\n" +
                "\tSELECT localidad_cvegeo, localidad_nombre, municipio_cvegeo, geom \n" +
                "\tFROM public.dim_localidad_rural \n" +
                "\tUNION\n" +
                "\tSELECT localidad_cvegeo, localidad_nombre, municipio_cvegeo, geom \n" +
                "\tFROM public.dim_localidad_urbana \n" +
                ") AS l \n" +
                "WHERE l.localidad_cvegeo = any(array[?]) \n";


        Connection conn = credentials.getConnection();
        System.out.println("query!!!!!!!!!!!!!!!!!!" + cvegeo);
        Array arrayCvegeo = conn.createArrayOf("text", cvegeo);
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);
        preparedStatement.setArray(1, arrayCvegeo);
        ResultSet rs = preparedStatement.executeQuery();

        while(rs.next()) {
            ProjectPostgresLocations projectPostgresLocations = new ProjectPostgresLocations();
            projectPostgresLocations.setLocalidad_cvegeo(rs.getString(1));
            projectPostgresLocations.setLocalidad_nombre(rs.getString(2));
            projectPostgresLocations.setMunicipio_cvegeo(rs.getString(3));
            projectPostgresLocations.setBbox(rs.getString(4));
            result.add(projectPostgresLocations);
        }

        rs.close();
        conn.close();
        return result;
    }
}
