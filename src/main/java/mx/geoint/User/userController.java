package mx.geoint.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mx.geoint.database.databaseController;

@CrossOrigin(origins = {"http://localhost:3000", "http://10.2.102.202:3000/","http://10.2.102.182"})
@RestController
@RequestMapping(path = "api/user")
public class userController {
    private final databaseController database;

    public userController(){
        database = new databaseController();
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public boolean createUser(@RequestBody User user){
        System.out.println("Create user");
        boolean creationUser = database.insertUser(user);

        return creationUser;
    }

}
