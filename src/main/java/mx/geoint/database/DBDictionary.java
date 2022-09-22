package mx.geoint.database;

import mx.geoint.Model.DictionaryDoc;
import mx.geoint.Response.DictionaryResponse;
import mx.geoint.Response.SearchResponse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBDictionary {
    public Credentials credentials;

    public DBDictionary(){
        this.credentials = new Credentials();
    }

    public DictionaryResponse ListBases(int offset, int noOfRecords) throws SQLException {
        ArrayList<DictionaryDoc> results = new ArrayList<DictionaryDoc>();
        DictionaryDoc dictionaryDoc = null;
        int totalHits = 0;

        Connection conn = credentials.getConnection();
        
        String SQL_QUERY = "SELECT * FROM bases offset " + offset + " limit " + noOfRecords;
        ResultSet rs = conn.prepareStatement(SQL_QUERY).executeQuery();

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

        rs = conn.prepareStatement("SELECT count(*) from bases").executeQuery();
        if (rs.next()) {
            totalHits = rs.getInt(1);
        }
        rs.close();

        conn.close();
        DictionaryResponse dictionaryResponse = new DictionaryResponse(results, totalHits);
        return dictionaryResponse;
    }
}
