package mx.geoint.Database;
import mx.geoint.Model.User.UserListResponse;
import mx.geoint.Model.User.UserResponse;
import mx.geoint.Model.User.UserRequest;

import java.time.LocalDateTime;
import java.sql.*;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.UUID;

public class DBUsers {
    public Credentials credentials;

    public DBUsers(){ this.credentials = new Credentials(); }

    public boolean insertUser(UserRequest userRequest) throws SQLException {
        System.out.println(userRequest.getNombre());
        System.out.println(userRequest.getApellido());
        System.out.println(userRequest.getCorreo());
        System.out.println(userRequest.getPassword());
        String encryptedPassword = org.apache.commons.codec.digest.DigestUtils.sha256Hex(userRequest.getPassword());

        System.out.println(encryptedPassword);

        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);

        //---guardado a base de datos
        Connection conn = credentials.getConnection();
        System.out.println(conn);

        String SQL_INSERT = "INSERT INTO usuarios (id_usuario, nombre, apellido, correo, contraseña, fecha_creacion) VALUES (?,?,?,?,?,?)";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT);
        preparedStatement.setObject(1, uuid);
        preparedStatement.setString(2, userRequest.getNombre());
        preparedStatement.setString(3, userRequest.getApellido());
        preparedStatement.setString(4, userRequest.getCorreo());
        preparedStatement.setString(5, encryptedPassword);
        preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));

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
    }

    /**
     * se encargar de verificar las credenciales de usuario y si existe devuelve su id
     * @param userRequest, User es la clase que contiene el correo y contraseña
     * @return UUID, String es el id_usuario
     */
    public UserResponse login(UserRequest userRequest) throws SQLException {
        String UUID = null;
        String encryptedPassword = org.apache.commons.codec.digest.DigestUtils.sha256Hex(userRequest.getPassword());
        ArrayList<UserListResponse> results = new ArrayList<UserListResponse>();
        UserListResponse userslist = null;
        int totalHits = 0;

        try {
            Connection conn = credentials.getConnection();
            String QUERY = "SELECT id_usuario, nombre, apellido, correo, permisos::text, id_rol FROM usuarios WHERE correo=? AND contraseña=?";

            PreparedStatement preparedStatement = conn.prepareStatement(QUERY);
            preparedStatement.setString(1, userRequest.getCorreo());
            preparedStatement.setString(2, encryptedPassword);

            ResultSet row = preparedStatement.executeQuery();
            while(row.next()){
                userslist = new UserListResponse();
                userslist.setUUID(row.getString(1));
                userslist.setNombres(row.getString(2));
                userslist.setApellidos(row.getString(3));
                userslist.setCorreo(row.getString(4));
                userslist.setPermisos(row.getString(5));
                userslist.setId_rol(row.getInt(6));
                results.add(userslist);
            }
            totalHits = results.size();

            row.close();
            preparedStatement.close();
            conn.close();

            UserResponse userResponse = new UserResponse(results, totalHits);
            return userResponse;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            return null;
        }
    }

    public Boolean haspermission(String uuid) {
        Boolean permission = false;
        try {
            Connection conn = credentials.getConnection();
            String QUERY = "select exists(select * from public.usuarios where id_usuario=?::uuid and id_rol = 1)";

            PreparedStatement preparedStatement = conn.prepareStatement(QUERY);
            preparedStatement.setString(1, uuid);
            ResultSet row = preparedStatement.executeQuery();
            while(row.next()){
                permission = row.getBoolean(1);
            }

            row.close();
            preparedStatement.close();
            conn.close();

            return permission;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            return permission;
        }
    }

    public UserResponse getUserslist() {
        ArrayList<UserListResponse> results = new ArrayList<UserListResponse>();
        UserListResponse userslist = null;
        int totalHits = 0;
        try {
            Connection conn = credentials.getConnection();
            String QUERY = "SELECT id_usuario, nombre, apellido, correo, permisos::text ,id_rol FROM public.usuarios WHERE id_rol =2 ORDER BY concat(nombre, apellido, correo) ASC;";

            PreparedStatement preparedStatement = conn.prepareStatement(QUERY);
            ResultSet row = preparedStatement.executeQuery();
            System.out.println(preparedStatement);
            while(row.next()){
                userslist = new UserListResponse();
                userslist.setUUID(row.getString(1));
                userslist.setNombres(row.getString(2));
                userslist.setApellidos(row.getString(3));
                userslist.setCorreo(row.getString(4));
                userslist.setPermisos(row.getString(5));
                results.add(userslist);
            }
            totalHits = results.size();

            row.close();
            preparedStatement.close();
            conn.close();

            UserResponse userResponse = new UserResponse(results, totalHits);
            return userResponse;

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return null;
    }

    public boolean updatePermissions(String uuid, String permissions) {
        try {
            Connection conn = credentials.getConnection();
            String QUERY = "UPDATE public.usuarios SET permisos=?::json WHERE id_usuario=?::uuid";

            PreparedStatement preparedStatement = conn.prepareStatement(QUERY);

            preparedStatement.setString(1, permissions);
            preparedStatement.setString(2, uuid);

            int row = preparedStatement.executeUpdate();

            //rows affected
            //System.out.println(row); //1.
            preparedStatement.close();
            conn.close();
            if(row>0){
                return true;
            }
            else{
                return false;
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            return false;
        }
    }
}
