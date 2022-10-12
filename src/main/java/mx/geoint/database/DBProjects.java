package mx.geoint.database;

import mx.geoint.Model.ProjectRegistration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBProjects {
    private Credentials credentials;

    public DBProjects(){
        this.credentials = new Credentials();
    }

    public ArrayList<ProjectRegistration> ListProjects() throws SQLException {
        ArrayList<ProjectRegistration> result = new ArrayList<>();
        ProjectRegistration projectRegistrations = null;
        String SQL_QUERY = "SELECT id_proyecto, id_usuario, nombre_proyecto, ruta_trabajo, fecha_creacion, estado, fecha_archivo, hablantes, ubicacion, radio, bounds " +
                            "FROM proyectos";

        Connection conn = credentials.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            int random_total = (int)Math.floor(Math.random()*(200-1+1)+1);
            int random_save = (int)Math.floor(Math.random()*(random_total-1+1)+1);

            projectRegistrations = new ProjectRegistration();
            projectRegistrations.setId_proyecto(rs.getString(1));
            projectRegistrations.setId_usuario(rs.getString(2));
            projectRegistrations.setNombre_proyecto(rs.getString(3));
            projectRegistrations.setRuta_trabajo(rs.getString(4));
            projectRegistrations.setFecha_creacion(rs.getString(5));
            projectRegistrations.setEstado(rs.getString(6));
            projectRegistrations.setFecha_archivo(rs.getString(7));
            projectRegistrations.setHablantes(rs.getString(8));
            projectRegistrations.setUbicacion(rs.getString(9));
            projectRegistrations.setRadio(rs.getInt(10));
            projectRegistrations.setBounds(rs.getString(11));


            projectRegistrations.setTotal_de_anotaciones(random_total);
            projectRegistrations.setAnotaciones_guardadas(random_save);
            result.add(projectRegistrations);
        }

        rs.close();
        conn.close();
        return result;
    }
}
