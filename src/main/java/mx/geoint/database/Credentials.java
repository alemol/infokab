package mx.geoint.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Credentials {
    String DBhost = "localhost";
    String DBname = "infokab";
    String DBuser = "postgres";
    String DBpassword = "postgres";
    String urlConnection = "jdbc:postgresql://"+DBhost+"/"+DBname;
    Properties props;

    public Credentials(){
        System.out.println("init databaseController");
        props = new Properties();
        props.setProperty("user",this.DBuser);
        props.setProperty("password",this.DBpassword);
    }

    public Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(this.urlConnection, props);
        return conn;
    }
}
