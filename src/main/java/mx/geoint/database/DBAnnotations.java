package mx.geoint.database;

import mx.geoint.Logger.Logger;
import mx.geoint.Model.GlosaAnnotationsRequest;
import mx.geoint.Model.Glosado.GlosadoAnnotationRegister;

import java.sql.*;
import java.util.ArrayList;

public class DBAnnotations {
    private Credentials credentials;
    private Logger logger;

    public DBAnnotations(){
        this.credentials = new Credentials();
        this.logger = new Logger();
    }

    public ArrayList<GlosadoAnnotationRegister> getListAnnotation(int proyecto_id) throws SQLException {
        ArrayList<GlosadoAnnotationRegister> rs = new ArrayList<>();
        GlosadoAnnotationRegister glosadoAnnotationRegister = null;
        String SQL_QUERY = "SELECT proyecto_id, anotacion_id, anotacion_ref, anotacion_tier_ref, glosado FROM glosado WHERE proyecto_id = $1";

        Connection conn = credentials.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);
        preparedStatement.setInt(1, proyecto_id);

        ResultSet row = preparedStatement.executeQuery();

        while(row.next()){
            glosadoAnnotationRegister = new GlosadoAnnotationRegister();
            glosadoAnnotationRegister.setProjectID(row.getInt(1));
            glosadoAnnotationRegister.setANNOTATION_ID(row.getString(1));
            glosadoAnnotationRegister.setANNOTATION_REF(row.getString(1));
            glosadoAnnotationRegister.setANNOTATION_TIER_REF(row.getString(1));
            glosadoAnnotationRegister.setGlosado(row.getString(1));
            rs.add(glosadoAnnotationRegister);
        }

        row.close();
        conn.close();
        return rs;
    }

    public GlosadoAnnotationRegister getRegister(int id) throws SQLException {
        GlosadoAnnotationRegister glosadoAnnotationRegister = null;
        String SQL_QUERY = "SELECT proyecto_id, anotacion_id, anotacion_ref, anotacion_tier_ref, glosado FROM glosado WHERE id = $1";

        Connection conn = credentials.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);
        preparedStatement.setInt(1, id);
        ResultSet row = preparedStatement.executeQuery();

        while(row.next()){
            glosadoAnnotationRegister = new GlosadoAnnotationRegister();
            glosadoAnnotationRegister.setANNOTATION_ID(row.getString(1));
            glosadoAnnotationRegister.setANNOTATION_REF(row.getString(1));
            glosadoAnnotationRegister.setANNOTATION_TIER_REF(row.getString(1));
            glosadoAnnotationRegister.setProjectID(row.getInt(1));
        }

        row.close();
        conn.close();
        return glosadoAnnotationRegister;
    }

    public boolean newRegister(GlosaAnnotationsRequest glosaAnnotationsRequest) throws SQLException {
        String annotationREF = "";
        if(glosaAnnotationsRequest.getAnnotationREF().isEmpty()){
            annotationREF = glosaAnnotationsRequest.getAnnotationID();
        }else{
            annotationREF = glosaAnnotationsRequest.getAnnotationREF();
        }

        Connection conn = credentials.getConnection();
        String SQL_INSERT = "INSERT INTO glosado (proyecto_id, anotacion_id, anotacion_ref, anotacion_tier_ref, glosado) VALUES(?,?,?,?,?,?,?) RETURNING proyecto_id";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, glosaAnnotationsRequest.getProjectID());
        preparedStatement.setString(2, glosaAnnotationsRequest.getAnnotationID());
        preparedStatement.setString(3, glosaAnnotationsRequest.getAnnotationREF());
        preparedStatement.setString(4, annotationREF);
        preparedStatement.setString(5, glosaAnnotationsRequest.getSteps().toString());
        int rs = preparedStatement.executeUpdate();

        conn.close();
        preparedStatement.close();
        if(rs>0){
            System.out.println("registro insertado en base de datos");
            return true;
        }else{
            System.out.println("No se pudo insertar el registro en base de datos");
            return false;
        }
    }

    public boolean updateRegister(GlosaAnnotationsRequest glosaAnnotationsRequest) throws SQLException {
        String annotationREF = "";
        if(glosaAnnotationsRequest.getAnnotationREF().isEmpty()){
            annotationREF = glosaAnnotationsRequest.getAnnotationID();
        }else{
            annotationREF = glosaAnnotationsRequest.getAnnotationREF();
        }

        Connection conn = credentials.getConnection();
        String SQL_UPDATE = "UPDATE glosado SET proyecto_id = ?, anotacion_id = ?, anotacion_ref = ?, anotacion_tier_ref = ?, glosado = ? WHERE proyecto_id=? and anotacion_id=? and anotacion_ref = ? and anotacion_tier_ref = ?";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE);
        preparedStatement.setInt(1, glosaAnnotationsRequest.getProjectID());
        preparedStatement.setString(2, glosaAnnotationsRequest.getAnnotationID());
        preparedStatement.setString(3, glosaAnnotationsRequest.getAnnotationREF());
        preparedStatement.setString(4, annotationREF);
        preparedStatement.setString(5, glosaAnnotationsRequest.getSteps().toString());
        preparedStatement.setInt(6, glosaAnnotationsRequest.getProjectID());
        preparedStatement.setString(7, glosaAnnotationsRequest.getAnnotationID());
        preparedStatement.setString(8, glosaAnnotationsRequest.getAnnotationREF());
        preparedStatement.setString(9, annotationREF);

        int rs = preparedStatement.executeUpdate();

        conn.close();
        preparedStatement.close();
        if(rs>0){
            System.out.println("registro insertado en base de datos");
            return true;
        }else{
            System.out.println("No se pudo insertar el registro en base de datos");
            return false;
        }
    }
}
