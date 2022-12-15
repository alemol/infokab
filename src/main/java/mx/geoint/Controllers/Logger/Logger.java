package mx.geoint.Controllers.Logger;

import mx.geoint.pathSystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    public static void appendToFile(Exception e) {
        try {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat dateFullFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String stringDate = dateFormat.format(date);
            String stringFullDate = dateFullFormat.format(date);
            System.out.println(stringDate);

            String existDirectory = existDirectory();
            FileWriter fstream = new FileWriter(existDirectory+stringDate+"-exception.log", true);
            BufferedWriter out = new BufferedWriter(fstream);
            PrintWriter pWriter = new PrintWriter(out, true);
            pWriter.print(stringFullDate+": ");
            e.printStackTrace(pWriter);
        }
        catch (Exception ie) {
            throw new RuntimeException("Could not write Exception to file", ie);
        }
    }

    private static String existDirectory(){
        String currentDirectory = pathSystem.DIRECTORY_LOG;

        if(!Files.exists(Path.of(currentDirectory))){
            File newSubDirectory = new File(currentDirectory);
            newSubDirectory.mkdirs();
        }

        return currentDirectory;
    }
}
