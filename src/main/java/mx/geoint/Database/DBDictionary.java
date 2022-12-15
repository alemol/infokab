package mx.geoint.Database;

import mx.geoint.Apis.Dictionary.DictionaryRequest;
import mx.geoint.Model.DictionaryDoc;
import mx.geoint.Response.DictionaryResponse;

import java.sql.*;
import java.util.ArrayList;

public class DBDictionary {
    public Credentials credentials;

    public DBDictionary(){
        this.credentials = new Credentials();
    }

    public DictionaryResponse ListRegistersDictionary(int offset, int noOfRecords, String Search, String tableName) throws SQLException {
        ArrayList<DictionaryDoc> results = new ArrayList<DictionaryDoc>();
        DictionaryDoc dictionaryDoc = null;
        int totalHits = 0;

        Connection conn = credentials.getConnection();

        String SQL_QUERY = "SELECT * FROM " + tableName +" WHERE clave like concat(?, '%') order by id offset " + offset + " limit " + noOfRecords;
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);
        preparedStatement.setObject(1, Search);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            dictionaryDoc = new DictionaryDoc();
            dictionaryDoc.setId(rs.getString(1));
            dictionaryDoc.setClave(rs.getString(2));
            dictionaryDoc.setCodigo(rs.getString(3));
            dictionaryDoc.setDescripcion(rs.getString(4));
            dictionaryDoc.setTraduccion(rs.getString(5));
            dictionaryDoc.setExtra(rs.getString(6));
            results.add(dictionaryDoc);
        }
        rs.close();

        SQL_QUERY = "SELECT count(*) FROM "+tableName+" WHERE clave like concat(?, '%')";
        preparedStatement = conn.prepareStatement(SQL_QUERY);
        preparedStatement.setObject(1, Search);
        rs = preparedStatement.executeQuery();
        if (rs.next()) {
            totalHits = rs.getInt(1);
        }
        rs.close();

        conn.close();
        DictionaryResponse dictionaryResponse = new DictionaryResponse(results, totalHits);
        return dictionaryResponse;
    }

    public boolean deleteRegisterDictionary(int register, String tableName) throws SQLException {
        Connection conn = credentials.getConnection();
        String SQL_QUERY = "DELETE FROM " + tableName +" WHERE id="+register;
        int rs = conn.prepareStatement(SQL_QUERY).executeUpdate();

        conn.close();
        if(rs>0){
            System.out.println("registro eliminado en base de datos");
            return true;
        } else{
            System.out.println("No se pudo eliminar el registro en base de datos");
            return false;
        }
    }

    public boolean insertRegisterDictionary(DictionaryRequest dictionaryRequest, String tableName) throws SQLException {
        Connection conn = credentials.getConnection();
        String SQL_INSERT = "INSERT INTO " + tableName +" (clave, codigo, descripcion, traduccion, extra) VALUES (?,?,?,?,?)";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT);
        preparedStatement.setObject(1, dictionaryRequest.getClave());
        preparedStatement.setString(2, dictionaryRequest.getCode());
        preparedStatement.setString(3, dictionaryRequest.getDescription());
        preparedStatement.setString(4, dictionaryRequest.getTraduction());
        preparedStatement.setString(5, dictionaryRequest.getExtra());

        int rs = preparedStatement.executeUpdate();

        conn.close();
        if(rs>0){
            System.out.println("registro insertado en base de datos");
            return true;
        } else{
            System.out.println("No se pudo insertar el registro en base de datos");
            return false;
        }
    }

    public boolean updateRegisterDictionary(DictionaryRequest dictionaryRequest, String tableName) throws SQLException {
        Connection conn = credentials.getConnection();
        String SQL_UPDATE = "UPDATE " + tableName +" SET clave = ?, codigo = ?, descripcion = ?, traduccion = ?, extra = ? WHERE id="+dictionaryRequest.getId();
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE);
        preparedStatement.setObject(1, dictionaryRequest.getClave());
        preparedStatement.setString(2, dictionaryRequest.getCode());
        preparedStatement.setString(3, dictionaryRequest.getDescription());
        preparedStatement.setString(4, dictionaryRequest.getTraduction());
        preparedStatement.setString(5, dictionaryRequest.getExtra());

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

    public ArrayList<String> textProcess(String text) throws SQLException {
        ArrayList<String> arrayList = new ArrayList<>();
        Connection conn = credentials.getConnection();
        String SQL_QUERY = QueryTextProcess.getQuery();
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);

        for(int i = 1; i <= 60; i++) {
            preparedStatement.setString(i, text);
        }
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            arrayList.add(rs.getString(2));
        }

        conn.close();
        return arrayList;
    }
}
