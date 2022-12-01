package mx.geoint.database;

import mx.geoint.Glosa.Dictionary.DictionaryRequest;
import mx.geoint.Logger.Logger;
import mx.geoint.Model.Glosado.GlosaUpdateAnnotationRequest;
import mx.geoint.Model.ReportDoc;
import mx.geoint.ParseXML.ParseXML;
import mx.geoint.Response.ReportsResponse;
import mx.geoint.pathSystem;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DBReports {
    public Credentials credentials;
    private Logger logger;

    public DBReports(){
        this.credentials = new Credentials();
        this.logger = new Logger();
    }

    public Boolean newRegister(int id_project, String title, String report, String type, String comentario, String anotacion) throws SQLException {
        Connection conn = credentials.getConnection();

        String SQL_INSERT = "INSERT INTO reportes (id_proyecto, titulo, reporte, tipo, activate, fecha_creacion, comentario, anotacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_proyecto";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setObject(1, id_project);
        preparedStatement.setString(2, title);
        preparedStatement.setString(3, report);
        preparedStatement.setString(4, type);
        preparedStatement.setBoolean(5, true);
        preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        preparedStatement.setString(7, comentario);
        preparedStatement.setString(8, anotacion);
        preparedStatement.execute();
        conn.close();

        return true;
    }

    public Boolean newAnnotationReport(GlosaUpdateAnnotationRequest glosaUpdateAnnotationRequest) throws SQLException {
        String projectName = glosaUpdateAnnotationRequest.getFilePath();
        String annotationId = glosaUpdateAnnotationRequest.getAnnotationID();
        String annotationValue = glosaUpdateAnnotationRequest.getAnnotationValue();
        String annotationOriginal = glosaUpdateAnnotationRequest.getAnnotationOriginal();

        int id_project = glosaUpdateAnnotationRequest.getProjectID();
        String title = glosaUpdateAnnotationRequest.getTitle();
        String report = glosaUpdateAnnotationRequest.getReport();
        String type = glosaUpdateAnnotationRequest.getType();

        Connection conn = credentials.getConnection();

        String SQL_INSERT = "INSERT INTO reportes (id_proyecto, titulo, reporte, tipo, activate, fecha_creacion, comentario, anotacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_proyecto";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setObject(1, id_project);
        preparedStatement.setString(2, title);
        preparedStatement.setString(3, report);
        preparedStatement.setString(4, type);
        preparedStatement.setBoolean(5, true);
        preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        preparedStatement.setString(7, annotationValue);
        preparedStatement.setString(8, annotationOriginal);
        preparedStatement.execute();

        boolean answer;
        try {
            ParseXML parseXML = new ParseXML(projectName, pathSystem.TIER_MAIN);
            parseXML.editAnnotation(annotationId, annotationValue, pathSystem.TIER_MAIN);
            answer = true;
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

    public ReportsResponse ListRegisters(int offset, int noOfRecords, Integer id, String search) throws SQLException {
        ArrayList<ReportDoc> results = new ArrayList<ReportDoc>();
        ReportDoc reportDoc = null;
        int totalHits = 0;

        Connection conn = credentials.getConnection();
        String SQL_QUERY = "";
        if(search == null){
            SQL_QUERY = "SELECT id, id_proyecto, titulo, reporte, fecha_creacion, tipo, activate, comentario, anotacion FROM reportes WHERE id_proyecto=? and activate=? order by id offset " + offset + " limit " + noOfRecords;
        }else{
            SQL_QUERY = "SELECT id, id_proyecto, titulo, reporte, fecha_creacion, tipo, activate, comentario, anotacion FROM reportes WHERE id_proyecto=? and activate=? and tipo=? order by id offset " + offset + " limit " + noOfRecords;
        }
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);
        preparedStatement.setObject(1, id);
        preparedStatement.setBoolean(2, true);

        if(search != null) {
            preparedStatement.setString(3, search);
        }

        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            reportDoc = new ReportDoc();
            reportDoc.setId(rs.getString(1));
            reportDoc.setId_proyecto(rs.getString(2));
            reportDoc.setTitulo(rs.getString(3));
            reportDoc.setReporte(rs.getString(4));
            reportDoc.setFecha_creacion(rs.getString(5));
            reportDoc.setTipo(rs.getString(6));
            reportDoc.setActivate(rs.getBoolean(7));
            reportDoc.setComentario(rs.getString(8));
            reportDoc.setAnotacion(rs.getString(9));
            results.add(reportDoc);
        }
        rs.close();

        if(search == null) {
            SQL_QUERY = "SELECT count(*) FROM reportes WHERE id_proyecto=? and activate=?";
        }else{
            SQL_QUERY = "SELECT count(*) FROM reportes WHERE id_proyecto=? and activate=? and tipo=?";
        }
        preparedStatement = conn.prepareStatement(SQL_QUERY);
        preparedStatement.setObject(1, id);
        preparedStatement.setBoolean(2, true);

        if(search != null) {
            preparedStatement.setString(3, search);
        }

        rs = preparedStatement.executeQuery();
        if (rs.next()) {
            totalHits = rs.getInt(1);
        }
        rs.close();

        conn.close();
        ReportsResponse reportsResponse = new ReportsResponse(results, totalHits);
        return reportsResponse;
    }

    public boolean deactivateAllReportes(int id_project) throws SQLException {
        Connection conn = credentials.getConnection();
        String SQL_UPDATE = "UPDATE reportes SET activate = ? WHERE id_proyecto=?";
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE);
        preparedStatement.setBoolean(1, false);
        preparedStatement.setInt(2, id_project);

        int rs = preparedStatement.executeUpdate();
        conn.close();
        if(rs>0){
            System.out.println("reportes desactivados actualizado en base de datos");
            return true;
        } else{
            System.out.println("No se pudo desactivar los reports en base de datos");
            return false;
        }
    }
}
