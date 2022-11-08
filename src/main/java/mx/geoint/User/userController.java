package mx.geoint.User;

import com.google.gson.JsonObject;
import mx.geoint.Logger.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mx.geoint.database.databaseController;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;

@CrossOrigin(origins = {"http://infokaab.com/","http://infokaab.com.mx/","http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009","http://10.2.102.182"})
@RestController
@RequestMapping(path = "api/user")
public class userController {
    private final databaseController database;
    private Logger logger;

    public userController(){
        database = new databaseController();
        this.logger = new Logger();
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public boolean createUser(@RequestBody User user) {
        try{
            System.out.println("Create user");
            boolean creationUser = database.insertUser(user);
            return creationUser;
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SQLException", e);
        }
    }

    /**
     *
     * @param user, User es la clase que contiene el correo y contraseña
     * @return
     */
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity loginUser(@RequestBody User user){
        try{
            String UUID = database.login(user);
            System.out.println("Get user : "+UUID);

            if(UUID != null && !UUID.isEmpty()){
                return createdResponseEntity(HttpStatus.OK, UUID, true);
            }else{
                return createdResponseEntity(HttpStatus.CONFLICT, "No existe usuario", false);
            }

        }catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SQLException", e);
        }
    }

    /**
     *
     * @param code HttpStatus, codigo Http
     * @param message String, Mensaje de respuesta
     * @param status boolean, respuesta del success
     * @return
     */
    public ResponseEntity createdResponseEntity(HttpStatus code, String message, boolean status){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");

        JsonObject answerJsonObject = new JsonObject();
        answerJsonObject.addProperty("success", status);

        if(status){
            answerJsonObject.addProperty("uuid", message);
        }else{
            answerJsonObject.addProperty("error", message);
        }

        return new ResponseEntity<>(answerJsonObject.toString(), headers, code);
    }

}
