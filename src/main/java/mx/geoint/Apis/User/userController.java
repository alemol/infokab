package mx.geoint.Apis.User;

import com.google.gson.JsonObject;
import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Database.DBUsers;
import mx.geoint.Model.User.UserRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import mx.geoint.Model.User.UserResponse;

import java.sql.SQLException;

@CrossOrigin(origins = {"http://infokaab.com/","http://infokaab.com.mx/","http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009","http://10.2.102.182"})
@RestController
@RequestMapping(path = "api/user")
public class userController {
    private final DBUsers database;
    private Logger logger;

    public userController(){
        database = new DBUsers();
        this.logger = new Logger();
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public boolean createUser(@RequestBody UserRequest userRequest) {
        try{
            System.out.println("Create user");
            boolean creationUser = database.insertUser(userRequest);

            return creationUser;
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SQLException", e);
        }
    }

    /**
     *
     * @param userRequest, User es la clase que contiene el correo y contraseña
     * @return
     */
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity loginUser(@RequestBody UserRequest userRequest){

        try{
            UserResponse userResponse = database.login(userRequest);
            if (userResponse.getTotalHits() ==1) {
                return ResponseEntity.status(HttpStatus.OK).body(userResponse);
            } else {
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

    @RequestMapping(path = "/list", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity usersList(@RequestParam String uuid) {

        Boolean haspermission = true;
        if (haspermission == true) {
            UserResponse userResponse = database.getUserslist();
            System.out.println(userResponse.getTotalHits());
            //return createdResponseEntity(HttpStatus.CONFLICT, "No tiene permiso", false);
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        } else {
            UserResponse userResponse = database.getUserslist();
            //return createdResponseEntity(HttpStatus.CONFLICT, "No tiene permiso", false);
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        }
    }

    @RequestMapping(path = "/updatePermissions", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity updatePermissions(@RequestParam String uuid, String permissions) {
        Boolean updated = database.updatePermissions(uuid, permissions);
        if (updated == true) {
            return ResponseEntity.status(HttpStatus.OK).body(updated);
        } else {
            return createdResponseEntity(HttpStatus.CONFLICT, "No se pudo actualizar", updated);
        }
    }

}