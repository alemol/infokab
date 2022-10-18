package mx.geoint.Logger;

import mx.geoint.pathSystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Logger {
    public static void appendToFile(Exception e) {
        try {
            existDirectory();
            FileWriter fstream = new FileWriter(pathSystem.DIRECTORY_LOG+"exception.log", true);
            BufferedWriter out = new BufferedWriter(fstream);
            PrintWriter pWriter = new PrintWriter(out, true);
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
