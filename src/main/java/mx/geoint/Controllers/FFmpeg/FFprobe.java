package mx.geoint.Controllers.FFmpeg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class FFprobe {
    String path;
    String s;

    public FFprobe() {
        this.s = null;
    }

    public String getMetadata(String source) throws IOException, InterruptedException {
        String command = "ffprobe -v quiet -print_format json  -show_format -show_streams " + source;

        Process proc = Runtime.getRuntime().exec(command);

        // Read the output

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(proc.getInputStream()));

        String response = reader.lines().collect(Collectors.joining());

        proc.waitFor();
        return response.replace(" ","");
    }
}


