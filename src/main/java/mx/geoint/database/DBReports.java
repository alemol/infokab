package mx.geoint.database;

import java.sql.*;
import java.time.LocalDateTime;

public class DBReports {
    public Credentials credentials;
    public DBReports(){
        this.credentials = new Credentials();
    }

    public Boolean newRegister(int id_project, String title, String report) throws SQLException {
        Connection conn = credentials.getConnection();

        String SQL_INSERT = "INSERT INTO reportes (id_proyecto, titulo, reporte, fecha_creacion) VALUES (?, ?, ?, ?) RETURNING id_proyecto";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setObject(1, id_project);
        preparedStatement.setString(2, title);
        preparedStatement.setString(3, report);
        preparedStatement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

        preparedStatement.execute();
        conn.close();

        return true;
    }
}
