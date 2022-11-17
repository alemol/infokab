package mx.geoint.database;

import mx.geoint.ParseXML.ParseXML;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DBReportsTest {
    private DBReports dbReports;

    @Test
    void newRegister() {
        try{
            DBReports dbReports = new DBReports();
            dbReports.newRegister(8,"Error Test", "Test Prueba","Documento");
        }catch (SQLException e){
            System.out.println("Error SQLEX");
            throw new RuntimeException(e);
        }
    }
}