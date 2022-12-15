package mx.geoint.database;

import mx.geoint.Database.DBReports;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class DBReportsTest {
    private DBReports dbReports;

    @Test
    void newRegister() {
        try{
            DBReports dbReports = new DBReports();
            dbReports.newRegister(8,"Error Test", "Test Prueba","document", "Este es un comentario", "");
        }catch (SQLException e){
            System.out.println("Error SQLEX");
            throw new RuntimeException(e);
        }
    }
}