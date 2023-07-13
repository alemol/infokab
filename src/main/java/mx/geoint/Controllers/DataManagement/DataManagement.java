package mx.geoint.Controllers.DataManagement;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import mx.geoint.Database.DBProjects.*;
import mx.geoint.Model.DataManagement.Metadatos;

import static mx.geoint.Database.DBProjects.getMetadata;

public class DataManagement {

    public long getFolderSize(File folder) {
        long length = 0;
        File[] files = folder.listFiles();

        int count = files.length;

        for (int i = 0; i < count; i++) {
            if (files[i].isFile()) {
                length += files[i].length();
            }
            else {
                length += getFolderSize(files[i]);
            }
        }
        return length;
    }

    public static Map<String, Object> getDiskSize(String path) throws SQLException {
        File diskPartition = new File(path);
        long totalCapacity = diskPartition.getTotalSpace() / (1000*1000*1000);
        long freePartitionSpace = diskPartition.getFreeSpace() / (1000*1000*1000);
        long usablePatitionSpace = diskPartition.getUsableSpace() / (1000*1000*1000);

        /*SELECT ruta_trabajo, metadata -> 'format' ->> 'duration' duration, metadata -> 'format' ->> 'size' tamaño, metadata -> 'streams' -> 0 ->>'channels' chanels, metadata
	FROM public.proyectos;*/

        ArrayList<Metadatos> metadata = getMetadata();

        Map<String, Object> mapSizes = new HashMap<String, Object>();
        mapSizes.put("totalCapacity", totalCapacity);
        mapSizes.put("freePartitionSpace", freePartitionSpace);
        mapSizes.put("usableParitionSpace", usablePatitionSpace);
        mapSizes.put("usedPartitionSpace", (totalCapacity - freePartitionSpace));
        mapSizes.put("metadata", metadata);

        return  mapSizes;

    }
}
