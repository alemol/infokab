package mx.geoint.User;

import com.google.gson.JsonObject;
import mx.geoint.Response.DictionaryResponse;
import mx.geoint.Response.UserListResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mx.geoint.database.databaseController;

import java.util.UUID;

//@CrossOrigin(origins = {"http://infokaab.com/","http://infokaab.com.mx/","http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009","http://10.2.102.182"})
@RestController
@RequestMapping(path = "api/user")
public class userController {
    private final databaseController database;

    public userController() {
        database = new databaseController();
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public boolean createUser(@RequestBody User user) {
        System.out.println("Create user");
        boolean creationUser = database.insertUser(user);

        return creationUser;
    }

    /**
     * @param user, User es la clase que contiene el correo y contraseña
     * @return
     */
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity loginUser(@RequestBody User user) {
        UserListResponse userListResponse = database.login(user);
        if (userListResponse.getTotalHits() ==1) {
            //return ResponseEntity.status(HttpStatus.OK,"success",true).body(userListResponse);
            return ResponseEntity.status(HttpStatus.OK).body(userListResponse);
        } else {
            return createdResponseEntity(HttpStatus.CONFLICT, "No existe usuario", false);
        }
    }

    /**
     * @param code    HttpStatus, codigo Http
     * @param message String, Mensaje de respuesta
     * @param status  boolean, respuesta del success
     * @return
     */
    public ResponseEntity createdResponseEntity(HttpStatus code, String message, boolean status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");

        JsonObject answerJsonObject = new JsonObject();
        answerJsonObject.addProperty("success", status);

        if (status) {
            answerJsonObject.addProperty("uuid", message);
        } else {
            answerJsonObject.addProperty("error", message);
        }

        return new ResponseEntity<>(answerJsonObject.toString(), headers, code);
    }

    /**
     * @param uuid recibimos el uuid del usuario que realiza la consulta
     * @return
     */
    @RequestMapping(path = "/list", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity usersList(@RequestParam String uuid) {
        /*Boolean haspermission = Boolean.valueOf(database.haspermission(uuid));
        System.out.println("Get usersList : "+haspermission);
        */
        Boolean haspermission = true;
        if (haspermission == true) {
            UserListResponse userListResponse = database.getUserslist();
            //return createdResponseEntity(HttpStatus.CONFLICT, "No tiene permiso", false);
            return ResponseEntity.status(HttpStatus.OK).body(userListResponse);
        } else {
            UserListResponse userListResponse = database.getUserslist();
            //return createdResponseEntity(HttpStatus.CONFLICT, "No tiene permiso", false);
            return ResponseEntity.status(HttpStatus.OK).body(userListResponse);
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
