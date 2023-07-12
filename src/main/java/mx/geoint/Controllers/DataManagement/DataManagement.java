package mx.geoint.Controllers.DataManagement;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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

    public static Map<String, Long> getDiskSize(String path){
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        //System.out.println("Free space: "+ FileSystemUtils.freeSpaceKb("/"));
        File diskPartition = new File(path);

        long totalCapacity = diskPartition.getTotalSpace() / (1024*1024*1024);


        long freePartitionSpace = diskPartition.getFreeSpace() / (1024*1024*1024);
        long usablePatitionSpace = diskPartition.getUsableSpace() / (1024*1024*1024);

        Map<String, Long> mapSizes = new HashMap<String, Long>();
        mapSizes.put("totalCapacity", totalCapacity);
        mapSizes.put("freePartitionSpace", freePartitionSpace);
        mapSizes.put("usableParitionSpace", usablePatitionSpace);
        mapSizes.put("usedPartitionSpace", (totalCapacity - freePartitionSpace));

        return  mapSizes;

    }
}
