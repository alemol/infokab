package mx.geoint.Controllers.DataManagement;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DataManagementTest {
    DataManagement dm = new DataManagement();

    @Test
    void getFolderSize() {
        long x = dm.getFolderSize(new File("/home/ulises/JavaApps/infokab"));
        System.out.println(x);
    }

    @Test
    void getDiskSize() throws SQLException {
        Map<String, Object> x = dm.getDiskSize("/");

        System.out.println("\n**** Sizes en Giga Bytes ****\n");

        System.out.println("Total  size : " + x.get("totalCapacity") + " GB");
        System.out.println("Usable Space : " + x.get("usablePartitionSpace") + " GB");
        System.out.println("Free Space : " + x.get("freePartitionSpace") + " GB");
        System.out.println("Used Space : " + x.get("usedPartitionSpace") + " GB");
    }
}