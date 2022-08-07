package mx.geoint.database;
import java.time.LocalDateTime;
import java.sql.*;
import java.util.Properties;
import java.util.UUID;

public class databaseController {

    public databaseController(){
        System.out.println("constructor");
    }
    public int createProject(String uuid, String basePath, String projectName){
        System.out.println("createProject");
        int id_project = 0;
        //---guardado a base de datos
        System.out.println("save to database: "+projectName);

        String url = "jdbc:postgresql://localhost/infokab";
        Properties props = new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","postgres");
        //props.setProperty("ssl","true");
        try {
            Connection conn = DriverManager.getConnection(url, props);
            System.out.println(conn);

            String SQL_INSERT = "INSERT INTO proyectos (id_usuario, nombre_proyecto, ruta_trabajo,fecha_creacion) VALUES (?,?,?,?) RETURNING id_proyecto";

            PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setObject(1, UUID.fromString(uuid));
            //preparedStatement.setObject();
            //preparedStatement.setString(1, uuid);
            preparedStatement.setString(2, projectName);
            preparedStatement.setString(3, basePath.toString());
            //preparedStatement.setString(4, contentType);
            preparedStatement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

            preparedStatement.execute();
            //int row = preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if ( rs.next() ) {
                // Retrieve the auto generated key(s).
                id_project = rs.getInt(1);
                System.out.println("generated key");
                System.out.println(id_project);
            }

            // rows affected
            //System.out.println(row); //1

/*            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM usuarios");
            while (rs.next())
            {
                System.out.print("Column 1 returned ");
                System.out.println(rs.getString(1));
            }
            rs.close();
            st.close();*/

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            //throw new RuntimeException(e);
            return 0;
        }
        //---guardado a base de datos

        return id_project;
    }
}
