package mx.geoint.Database;

import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Model.Search.SearchPostgres;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.UUID;

public class DBSearch {
    private static Credentials credentials;
    private Logger logger;

    public DBSearch(){
        this.credentials = new Credentials();
        this.logger = new Logger();
    }

    public int createSearch(String uuid, String search, String index) throws SQLException {
        System.out.println("createSearch");
        int id_search = 0;
        System.out.println("save to database: " + search);
        Connection conn = credentials.getConnection();
        System.out.println(conn);
        System.out.println(index);

        String SQL_INSERT = "INSERT INTO busquedas (id_usuario, consulta, fecha_creacion, indice) " +
                "VALUES (?, ?, ?, ?) RETURNING id_busqueda";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setObject(1, null);
        preparedStatement.setString(2, search);
        preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
        preparedStatement.setString(4, index);

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

    public ArrayList<SearchPostgres> listSearch() throws SQLException {
        ArrayList<SearchPostgres> result = new ArrayList<>();
        SearchPostgres searchPostgres = null;

        String SQL_QUERY = "select * FROM busquedas";

        Connection conn = credentials.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            searchPostgres = new SearchPostgres();
            searchPostgres.setId_busqueda(rs.getString(1));
            searchPostgres.setId_usuario(rs.getString(2));
            searchPostgres.setConsulta(rs.getString(3));
            searchPostgres.setFecha_creacion(rs.getString(4));
            searchPostgres.setIndice(rs.getString(5));

            result.add(searchPostgres);
        }
        rs.close();
        conn.close();
        return result;
    }
}
