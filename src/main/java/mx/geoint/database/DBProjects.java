package mx.geoint.database;

import mx.geoint.Glosa.Dictionary.DictionaryRequest;
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

    public ProjectRegistration getProjectById(String id) throws  SQLException {
        String SQL_QUERY = "SELECT p.id_proyecto, p.nombre_proyecto, p.ruta_trabajo FROM proyectos as p WHERE p.id_proyecto = " + id;

        Connection conn = credentials.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);
        ResultSet rs = preparedStatement.executeQuery();

        ProjectRegistration projectRegister = null;

        while(rs.next()) {
            projectRegister = new ProjectRegistration();
            projectRegister.setId_proyecto(rs.getString(1));
            projectRegister.setNombre_proyecto(rs.getString(2));
            projectRegister.setRuta_trabajo(rs.getString(3));
        }
        rs.close();
        conn.close();
        return projectRegister;

    }

    public ArrayList<ProjectRegistration> ListProjects() throws SQLException {
        ArrayList<ProjectRegistration> result = new ArrayList<>();
        ProjectRegistration projectRegistrations = null;
        String SQL_QUERY = "select\n" +
                            "p.*, count(r.id_proyecto) as total_de_reportes\n" +
                            "FROM proyectos as p\n" +
                            "left join reportes as r on r.id_proyecto = p.id_proyecto\n" +
                            "group by p.id_proyecto\n" +
                            "order by p.id_proyecto";

        Connection conn = credentials.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(SQL_QUERY);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            int random_total = rs.getInt(12);
            int random_save = 0;

            if(random_total > 0){
                random_save = (int)Math.floor(Math.random()*(random_total-1+1)+1);
            }

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
            projectRegistrations.setTotal_de_reportes(rs.getInt(14));

            projectRegistrations.setTotal_de_anotaciones(random_total);
            projectRegistrations.setAnotaciones_guardadas(random_save);
            result.add(projectRegistrations);
        }

        rs.close();
        conn.close();
        return result;
    }

    public boolean setProjectAnnotationsCounter(Integer id_project, Integer count) throws SQLException {
        Connection conn = credentials.getConnection();
        String SQL_UPDATE = "UPDATE proyectos SET total_de_anotaciones = ? WHERE id_proyecto=?";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE);
        preparedStatement.setInt(1, count);
        preparedStatement.setObject(2, id_project);
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
}
