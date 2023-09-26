package mx.geoint.Database;

import mx.geoint.Controllers.Logger.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class DBSearch {
    private static Credentials credentials;
    private Logger logger;

    public DBSearch(){
        this.credentials = new Credentials();
        this.logger = new Logger();
    }

    public int createSearch(String uuid, String search) throws SQLException {
        System.out.println("createSearch");
        int id_search = 0;
        System.out.println("save to database: " + search);
        Connection conn = credentials.getConnection();
        System.out.println(conn);

        String SQL_INSERT = "INSERT INTO busquedas (id_usuario, consulta, fecha_creacion) " +
                "VALUES (?, ?, ?) RETURNING id_busqueda";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setObject(1, UUID.fromString(uuid));
        preparedStatement.setString(2, search);
        preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));

        preparedStatement.execute();
        //int row = preparedStatement.executeUpdate();
        ResultSet rs = preparedStatement.getGeneratedKeys();
        if (rs.next()) {
            // Retrieve the auto generated key(s).
            id_search = rs.getInt(1);
            System.out.println("generated key");
            System.out.println(id_search);
        }

        conn.close();

        return id_search;
    }
}
