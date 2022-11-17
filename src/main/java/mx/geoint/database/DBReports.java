package mx.geoint.database;

import mx.geoint.Glosa.Dictionary.DictionaryRequest;
import mx.geoint.Model.ReportDoc;
import mx.geoint.Response.ReportsResponse;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DBReports {
    public Credentials credentials;
    public DBReports(){
        this.credentials = new Credentials();
    }

    public Boolean newRegister(int id_project, String title, String report, String type) throws SQLException {
        Connection conn = credentials.getConnection();

        String SQL_INSERT = "INSERT INTO reportes (id_proyecto, titulo, reporte, tipo, activate, fecha_creacion) VALUES (?, ?, ?, ?, ?, ?) RETURNING id_proyecto";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setObject(1, id_project);
        preparedStatement.setString(2, title);
        preparedStatement.setString(3, report);
        preparedStatement.setString(4, type);
        preparedStatement.setBoolean(5, true);
        preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        preparedStatement.execute();
        conn.close();

        return true;
    }

    public ReportsResponse ListRegisters(int offset, int noOfRecords, Integer id) throws SQLException {
        ArrayList<ReportDoc> results = new ArrayList<ReportDoc>();
        ReportDoc reportDoc = null;
        int totalHits = 0;

        Connection conn = credentials.getConnection();

        String SQL_QUERY = "SELECT * FROM reportes WHERE id_proyecto=? and activate=? order by id offset " + offset + " limit " + noOfRecords;
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);
        preparedStatement.setObject(1, id);
        preparedStatement.setBoolean(2, true);
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
            results.add(reportDoc);
        }
        rs.close();

        SQL_QUERY = "SELECT count(*) FROM reportes WHERE id_proyecto=? and activate=?";
        preparedStatement = conn.prepareStatement(SQL_QUERY);
        preparedStatement.setObject(1, id);
        preparedStatement.setBoolean(2, true);
        rs = preparedStatement.executeQuery();
        if (rs.next()) {
            totalHits = rs.getInt(1);
        }
        rs.close();

        conn.close();
        ReportsResponse reportsResponse = new ReportsResponse(results, totalHits);
        return reportsResponse;
    }
}
