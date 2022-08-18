package mx.geoint.database;
import mx.geoint.User.User;

import java.time.LocalDateTime;
import java.sql.*;
import java.util.Properties;
import java.util.UUID;

public class databaseController {

    String DBhost = "localhost";
    String DBname = "infokab";
    String DBuser = "postgres";
    String DBpassword = "postgres";
    String urlConnection = "jdbc:postgresql://"+DBhost+"/"+DBname;

    Properties props;

    public databaseController(){
        System.out.println("init databaseController");
        props = new Properties();
        props.setProperty("user",this.DBuser);
        props.setProperty("password",this.DBpassword);
    }
    public int createProject(String uuid, String basePath, String projectName){
        System.out.println("createProject");
        int id_project = 0;
        //---guardado a base de datos
        System.out.println("save to database: "+projectName);

        //String url = "jdbc:postgresql://localhost/infokab";
        //Properties props = new Properties();
        //props.setProperty("user",this.DBuser);
        //props.setProperty("password",this.DBpassword);
        //props.setProperty("ssl","true");
        try {
            Connection conn = DriverManager.getConnection(this.urlConnection, props);
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
            conn.close();
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

    public boolean insertUser(User user){
        System.out.println(user.getNombre());
        System.out.println(user.getApellido());
        System.out.println(user.getCorreo());
        System.out.println(user.getPassword());
        String encryptedPassword = org.apache.commons.codec.digest.DigestUtils.sha256Hex(user.getPassword());

        System.out.println(encryptedPassword);

        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);

        //---guardado a base de datos

        //Properties props = new Properties();
        //props.setProperty("user",this.DBuser);
        //props.setProperty("password",this.DBpassword);
        //props.setProperty("ssl","true");
        try {
            Connection conn = DriverManager.getConnection(this.urlConnection, props);
            System.out.println(conn);

            String SQL_INSERT = "INSERT INTO usuarios (id_usuario, nombre, apellido, correo, contraseña, fecha_creacion) VALUES (?,?,?,?,?,?)";

            PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT);
            preparedStatement.setObject(1, uuid);
            preparedStatement.setString(2, user.getNombre());
            preparedStatement.setString(3, user.getApellido());
            preparedStatement.setString(4, user.getCorreo());
            preparedStatement.setString(5, encryptedPassword);
            preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));

            int row = preparedStatement.executeUpdate();

            //rows affected
            System.out.println(row); //1
            conn.close();
            if(row>0){
                System.out.println("usuario insertado en base de datos");
                return true;
            }
            else{
                System.out.println("No se pudo insertar el usuario en base de datos");
                return false;
            }



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
            return false;
        }
        //---guardado a base de datos

    }

    /**
     * se encargar de verificar las credenciales de usuario y si existe devuelve su id
     * @param user, User es la clase que contiene el correo y contraseña
     * @return UUID, String es el id_usuario
     */
    public String login(User user){
        String UUID = null;
        String encryptedPassword = org.apache.commons.codec.digest.DigestUtils.sha256Hex(user.getPassword());

        try {
            Connection conn = DriverManager.getConnection(this.urlConnection, props);
            String QUERY = "SELECT id_usuario FROM usuarios WHERE correo=? AND contraseña=?";

            PreparedStatement preparedStatement = conn.prepareStatement(QUERY);
            preparedStatement.setString(1, user.getCorreo());
            preparedStatement.setString(2, encryptedPassword);

            ResultSet row = preparedStatement.executeQuery();
            while(row.next()){
                UUID = row.getString(1);
            }

            row.close();
            preparedStatement.close();
            conn.close();

            return UUID;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            return null;
        }
    }
}
