package mx.geoint.database;

import com.google.gson.Gson;
import mx.geoint.Logger.Logger;
import mx.geoint.Model.GlosaAnnotationsRequest;
import mx.geoint.Model.GlosaStep;
import mx.geoint.Model.Glosado.GlosadoAnnotationRegister;
import mx.geoint.ParseXML.ParseXML;
import org.json.JSONArray;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;

public class DBAnnotations {
    private Credentials credentials;
    private Logger logger;

    public DBAnnotations(){
        this.credentials = new Credentials();
        this.logger = new Logger();
    }

    public ArrayList<GlosadoAnnotationRegister> getAnnotationList(int project_id) throws SQLException {
        ArrayList<GlosadoAnnotationRegister> rs = new ArrayList<>();
        GlosadoAnnotationRegister glosadoAnnotationRegister = null;
        String SQL_QUERY = "SELECT proyecto_id, anotacion_id, anotacion_ref, anotacion_tier_ref, glosado FROM glosado WHERE proyecto_id = ?";

        Connection conn = credentials.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);
        preparedStatement.setInt(1, project_id);

        ResultSet row = preparedStatement.executeQuery();

        while(row.next()){
            glosadoAnnotationRegister = new GlosadoAnnotationRegister();
            glosadoAnnotationRegister.setProjectID(row.getInt(1));
            glosadoAnnotationRegister.setANNOTATION_ID(row.getString(2));
            glosadoAnnotationRegister.setANNOTATION_REF(row.getString(3));
            glosadoAnnotationRegister.setANNOTATION_TIER_REF(row.getString(4));

            ArrayList<GlosaStep> steps = new Gson().fromJson(row.getString(5), ArrayList.class);
            glosadoAnnotationRegister.setSteps(steps);

            rs.add(glosadoAnnotationRegister);
        }

        row.close();
        conn.close();
        return rs;
    }

    public GlosadoAnnotationRegister getAnnotationRecord(int project_id, String annotation_ref) throws SQLException {
        GlosadoAnnotationRegister glosadoAnnotationRegister = null;
        String SQL_QUERY = "SELECT proyecto_id, anotacion_id, anotacion_ref, anotacion_tier_ref, glosado FROM glosado WHERE proyecto_id = ? and anotacion_ref = ?";

        Connection conn = credentials.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);
        preparedStatement.setInt(1, project_id);
        preparedStatement.setString(2, annotation_ref);

        ResultSet row = preparedStatement.executeQuery();

        while(row.next()){
            glosadoAnnotationRegister = new GlosadoAnnotationRegister();
            glosadoAnnotationRegister.setProjectID(row.getInt(1));
            glosadoAnnotationRegister.setANNOTATION_ID(row.getString(2));
            glosadoAnnotationRegister.setANNOTATION_REF(row.getString(3));
            glosadoAnnotationRegister.setANNOTATION_TIER_REF(row.getString(4));

            ArrayList<GlosaStep> steps = new Gson().fromJson(row.getString(5), ArrayList.class);
            glosadoAnnotationRegister.setSteps(steps);
        }

        row.close();
        conn.close();
        return glosadoAnnotationRegister;
    }

    public boolean newRegister(GlosaAnnotationsRequest glosaAnnotationsRequest) throws SQLException {
        String projectName = glosaAnnotationsRequest.getFilePath();
        int projectID = glosaAnnotationsRequest.getProjectID();
        String annotationId = glosaAnnotationsRequest.getAnnotationID();
        ArrayList<GlosaStep> steps = glosaAnnotationsRequest.getSteps();
        String stepsString = new Gson().toJson(steps);

        String annotationREF = "";
        if(glosaAnnotationsRequest.getAnnotationREF().isEmpty()){
            annotationREF = glosaAnnotationsRequest.getAnnotationID();
        }else{
            annotationREF = glosaAnnotationsRequest.getAnnotationREF();
        }

        Connection conn = credentials.getConnection();
        conn.setAutoCommit(false);
        String SQL_INSERT = "INSERT INTO glosado (proyecto_id, anotacion_id, anotacion_ref, anotacion_tier_ref, glosado) VALUES(?,?,?,?,?) RETURNING proyecto_id";


        PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, projectID);
        preparedStatement.setString(2, 'g'+annotationREF);
        preparedStatement.setString(3, annotationREF);
        preparedStatement.setString(4, annotationId);
        preparedStatement.setString(5, stepsString);
        int rs = preparedStatement.executeUpdate();
        boolean answer = false;

        try{
            ParseXML parseXML = new ParseXML(projectName, "Glosado");
            parseXML.writeElement(annotationREF, annotationId, steps);
            conn.commit();

            if(rs>0){
                System.out.println("registro insertado en base de datos");
                answer = true;
            }else{
                System.out.println("No se pudo insertar el registro en base de datos");
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

    public boolean updateRegister(GlosaAnnotationsRequest glosaAnnotationsRequest) throws SQLException {
        String projectName = glosaAnnotationsRequest.getFilePath();
        int projectID = glosaAnnotationsRequest.getProjectID();
        String annotationId = glosaAnnotationsRequest.getAnnotationID();
        ArrayList<GlosaStep> steps = glosaAnnotationsRequest.getSteps();
        String stepsString = new Gson().toJson(steps);

        String annotationREF = "";
        if(glosaAnnotationsRequest.getAnnotationREF().isEmpty()){
            annotationREF = glosaAnnotationsRequest.getAnnotationID();
        }else{
            annotationREF = glosaAnnotationsRequest.getAnnotationREF();
        }

        Connection conn = credentials.getConnection();
        conn.setAutoCommit(false);
        String SQL_UPDATE = "UPDATE glosado SET proyecto_id = ?, anotacion_id = ?, anotacion_ref = ?, anotacion_tier_ref = ?, glosado = ? WHERE proyecto_id=? and anotacion_id=? and anotacion_ref = ? and anotacion_tier_ref = ?";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE);
        preparedStatement.setInt(1, projectID);
        preparedStatement.setString(2, 'g'+annotationREF);
        preparedStatement.setString(3, annotationREF);
        preparedStatement.setString(4, annotationId);
        preparedStatement.setString(5, stepsString);
        preparedStatement.setInt(6, glosaAnnotationsRequest.getProjectID());
        preparedStatement.setString(7, glosaAnnotationsRequest.getAnnotationID());
        preparedStatement.setString(8, glosaAnnotationsRequest.getAnnotationREF());
        preparedStatement.setString(9, annotationREF);

        int rs = preparedStatement.executeUpdate();
        boolean answer = false;

        try{
            ParseXML parseXML = new ParseXML(projectName, "Glosado");
            parseXML.writeElement(annotationREF, annotationId, steps);
            conn.commit();

            if(rs>0){
                System.out.println("registro insertado en base de datos");
                answer = true;
            }else{
                System.out.println("No se pudo insertar el registro en base de datos");
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

    public boolean deleteGlossingRecords(int projectID) throws SQLException {
        Connection conn = credentials.getConnection();
        String SQL_QUERY = "DELETE FROM glosado WHERE proyecto_id=?";
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);
        preparedStatement.setInt(1, projectID);
        int rs = preparedStatement.executeUpdate();

        conn.close();
        if(rs>0){
            System.out.println("registro eliminado en base de datos");
            return true;
        } else{
            System.out.println("No se pudo eliminar el registro en base de datos");
            return false;
        }
    }
}
