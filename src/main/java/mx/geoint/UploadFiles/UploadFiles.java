package mx.geoint.UploadFiles;

import mx.geoint.ElanXmlDigester.ThreadElanXmlDigester;
import mx.geoint.pathSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.sql.*;
import java.util.Properties;
import java.util.UUID;

@Component
public class UploadFiles {
    //@Autowired
    //ThreadElanXmlDigester threadElanXmlDigester;
    private final ThreadElanXmlDigester threadElanXmlDigester;
    public UploadFiles(){
        threadElanXmlDigester = new ThreadElanXmlDigester();
        threadElanXmlDigester.start();
    }

    public boolean uploadFile(MultipartFile eaf, MultipartFile multimedia, String uuid) throws IOException {
        if(!saveFile(eaf, uuid, pathSystem.DIRECTORY_ANNOTATION)){
            return false;
        }

        if(!saveFile(multimedia, uuid, pathSystem.DIRECTORY_MULTIMEDIA)){
            return false;
        }



        InitElanXmlDigester(eaf, uuid);
        return true;
    }

    public void InitElanXmlDigester(MultipartFile eaf, String uuid){
        String name = eaf.getOriginalFilename();
        String pathEaf = pathSystem.DIRECTORY_ANNOTATION+uuid+"/"+name;
        threadElanXmlDigester.add(pathEaf, uuid);
        threadElanXmlDigester.activate();
    }

    public boolean saveFile(MultipartFile file, String uuid, String directory) throws IOException {
        String name = file.getOriginalFilename();
        String contentType = file.getContentType();
        long size = file.getSize();

        String currentDirectory = existDirectory(directory, uuid);

        Path path = Paths.get(currentDirectory + name);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("PATH :" + name);
        System.out.println("contentType :" + contentType);
        System.out.println("Size :" + size);

        //---guardado a base de datos
        System.out.println("save to database: "+file.getOriginalFilename());

        String url = "jdbc:postgresql://localhost/infokab";
        Properties props = new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","postgres");
        //props.setProperty("ssl","true");
        try {
            Connection conn = DriverManager.getConnection(url, props);
            System.out.println(conn);

            String SQL_INSERT = "INSERT INTO archivos (id_usuario, nombre, ruta_trabajo,tipo_archivo,fecha_creacion) VALUES (?,?,?,?,?)";

            PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT);
            preparedStatement.setObject(1, UUID.fromString(uuid));
            //preparedStatement.setObject();
            //preparedStatement.setString(1, uuid);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, currentDirectory);
            preparedStatement.setString(4, contentType);
            preparedStatement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

            int row = preparedStatement.executeUpdate();

            // rows affected
            System.out.println(row); //1

/*            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM usuarios");
            while (rs.next())
            {
                System.out.print("Column 1 returned ");
                System.out.println(rs.getString(1));
            }
            rs.close();
            st.close();*/

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            //throw new RuntimeException(e);
            return false;
        }
        //---guardado a base de datos

        return true;
    }

    private String existDirectory(String pathDirectory, String uuid){
        String currentDirectory = pathDirectory + uuid + "/";

        if(!Files.exists(Path.of(currentDirectory))){
            if (!Files.exists(Path.of(pathDirectory))){
                File newDirectory = new File(pathDirectory);
                newDirectory.mkdir();
            }

            File newSubDirectory = new File(currentDirectory);
            newSubDirectory.mkdir();
        }

        return currentDirectory;
    }
}
