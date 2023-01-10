package mx.geoint.Database;

import com.google.gson.Gson;
import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Controllers.WriteXML.WriteXML;
import mx.geoint.Model.Annotation.AnnotationsRequest;
import mx.geoint.Model.Glosado.GlosaStep;
import mx.geoint.Model.Annotation.AnnotationRegister;
import mx.geoint.Controllers.ParseXML.ParseXML;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class DBAnnotations {
    private Credentials credentials;
    private Logger logger;

    public DBAnnotations(){
        this.credentials = new Credentials();
        this.logger = new Logger();
    }

    public ArrayList<AnnotationRegister> getAnnotationList(int project_id) throws SQLException {
        ArrayList<AnnotationRegister> rs = new ArrayList<>();
        AnnotationRegister annotationRegister = null;
        String SQL_QUERY = "SELECT proyecto_id, anotacion_id, anotacion_ref, anotacion_tier_ref, glosado FROM glosado WHERE proyecto_id = ?";

        Connection conn = credentials.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);
        preparedStatement.setInt(1, project_id);

        ResultSet row = preparedStatement.executeQuery();

        while(row.next()){
            annotationRegister = new AnnotationRegister();
            annotationRegister.setProjectID(row.getInt(1));
            annotationRegister.setANNOTATION_ID(row.getString(2));
            annotationRegister.setANNOTATION_REF(row.getString(3));
            annotationRegister.setANNOTATION_TIER_REF(row.getString(4));

            //ArrayList<GlosaStep> steps = new Gson().fromJson(row.getString(5), ArrayList.class);
            GlosaStep[] arraySteps = new Gson().fromJson(row.getString(5), GlosaStep[].class);
            ArrayList<GlosaStep> listSteps = new ArrayList<GlosaStep>();
            Collections.addAll(listSteps, arraySteps);

            annotationRegister.setSteps(listSteps);

            rs.add(annotationRegister);
        }

        row.close();
        conn.close();
        return rs;
    }

    public AnnotationRegister getAnnotationRecord(int project_id, String annotation_ref) throws SQLException {
        AnnotationRegister annotationRegister = null;
        String SQL_QUERY = "SELECT proyecto_id, anotacion_id, anotacion_ref, anotacion_tier_ref, glosado FROM glosado WHERE proyecto_id = ? and anotacion_ref = ?";

        Connection conn = credentials.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);
        preparedStatement.setInt(1, project_id);
        preparedStatement.setString(2, annotation_ref);

        ResultSet row = preparedStatement.executeQuery();

        while(row.next()){
            annotationRegister = new AnnotationRegister();
            annotationRegister.setProjectID(row.getInt(1));
            annotationRegister.setANNOTATION_ID(row.getString(2));
            annotationRegister.setANNOTATION_REF(row.getString(3));
            annotationRegister.setANNOTATION_TIER_REF(row.getString(4));

            ArrayList<GlosaStep> steps = new Gson().fromJson(row.getString(5), ArrayList.class);
            annotationRegister.setSteps(steps);
        }

        row.close();
        conn.close();
        return annotationRegister;
    }

    public boolean newRegister(AnnotationsRequest annotationsRequest) throws SQLException {
        String projectName = annotationsRequest.getFilePath();
        int projectID = annotationsRequest.getProjectID();
        String annotationId = annotationsRequest.getAnnotationID();
        ArrayList<GlosaStep> steps = annotationsRequest.getSteps();
        String stepsString = new Gson().toJson(steps);

        String annotationREF = "";
        if(annotationsRequest.getAnnotationREF().isEmpty()){
            annotationREF = annotationsRequest.getAnnotationID();
        }else{
            annotationREF = annotationsRequest.getAnnotationREF();
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
            WriteXML writeXML = new WriteXML(projectName);
            writeXML.writeElement(annotationREF, annotationId, steps);
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

    public boolean updateRegister(AnnotationsRequest annotationsRequest) throws SQLException {
        String projectName = annotationsRequest.getFilePath();
        int projectID = annotationsRequest.getProjectID();
        String annotationId = annotationsRequest.getAnnotationID();
        ArrayList<GlosaStep> steps = annotationsRequest.getSteps();
        String stepsString = new Gson().toJson(steps);

        String annotationREF = "";
        if(annotationsRequest.getAnnotationREF().isEmpty()){
            annotationREF = annotationsRequest.getAnnotationID();
        }else{
            annotationREF = annotationsRequest.getAnnotationREF();
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
        preparedStatement.setInt(6, annotationsRequest.getProjectID());
        preparedStatement.setString(7, annotationsRequest.getAnnotationID());
        preparedStatement.setString(8, annotationsRequest.getAnnotationREF());
        preparedStatement.setString(9, annotationREF);

        int rs = preparedStatement.executeUpdate();
        boolean answer = false;

        try{
            WriteXML writeXML = new WriteXML(projectName);
            writeXML.writeElement(annotationREF, annotationId, steps);
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


    public boolean newRegisterV3(AnnotationsRequest annotationsRequest) throws SQLException {
        String projectName = annotationsRequest.getFilePath();
        int projectID = annotationsRequest.getProjectID();
        String annotationId = annotationsRequest.getAnnotationID();
        ArrayList<GlosaStep> steps = annotationsRequest.getSteps();
        String stepsString = new Gson().toJson(steps);

        String annotationREF = "";
        if(annotationsRequest.getAnnotationREF().isEmpty()){
            annotationREF = annotationsRequest.getAnnotationID();
        }else{
            annotationREF = annotationsRequest.getAnnotationREF();
        }

        Connection conn = credentials.getConnection();
        String SQL_INSERT = "INSERT INTO glosado (proyecto_id, anotacion_id, anotacion_ref, anotacion_tier_ref, glosado) VALUES(?,?,?,?,?) RETURNING proyecto_id";


        PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, projectID);
        preparedStatement.setString(2, 'g'+annotationREF);
        preparedStatement.setString(3, annotationREF);
        preparedStatement.setString(4, annotationId);
        preparedStatement.setString(5, stepsString);
        int rs = preparedStatement.executeUpdate();
        boolean answer = false;

        if(rs>0){
            System.out.println("registro insertado en base de datos");
            answer = true;
        }else{
            System.out.println("No se pudo insertar el registro en base de datos");
            answer = false;
        }

        preparedStatement.close();
        conn.close();
        return answer;
    }

    public boolean updateRegisterV3(AnnotationsRequest annotationsRequest) throws SQLException {
        String projectName = annotationsRequest.getFilePath();
        int projectID = annotationsRequest.getProjectID();
        String annotationId = annotationsRequest.getAnnotationID();
        ArrayList<GlosaStep> steps = annotationsRequest.getSteps();
        String stepsString = new Gson().toJson(steps);

        String annotationREF = "";
        if(annotationsRequest.getAnnotationREF().isEmpty()){
            annotationREF = annotationsRequest.getAnnotationID();
        }else{
            annotationREF = annotationsRequest.getAnnotationREF();
        }

        Connection conn = credentials.getConnection();
        String SQL_UPDATE = "UPDATE glosado SET proyecto_id = ?, anotacion_id = ?, anotacion_ref = ?, anotacion_tier_ref = ?, glosado = ? WHERE proyecto_id=? and anotacion_id=? and anotacion_ref = ? and anotacion_tier_ref = ?";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE);
        preparedStatement.setInt(1, projectID);
        preparedStatement.setString(2, 'g'+annotationREF);
        preparedStatement.setString(3, annotationREF);
        preparedStatement.setString(4, annotationId);
        preparedStatement.setString(5, stepsString);
        preparedStatement.setInt(6, annotationsRequest.getProjectID());
        preparedStatement.setString(7, annotationsRequest.getAnnotationID());
        preparedStatement.setString(8, annotationsRequest.getAnnotationREF());
        preparedStatement.setString(9, annotationREF);

        int rs = preparedStatement.executeUpdate();
        boolean answer = false;

        if(rs>0){
            System.out.println("registro insertado en base de datos");
            answer = true;
        }else{
            System.out.println("No se pudo insertar el registro en base de datos");
            answer = false;
        }

        preparedStatement.close();
        conn.close();
        return answer;
    }
}
