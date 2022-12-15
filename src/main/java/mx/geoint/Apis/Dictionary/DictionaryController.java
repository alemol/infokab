package mx.geoint.Apis.Dictionary;

import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Model.Dictionary.DictionaryPaginate;
import mx.geoint.Model.Dictionary.DictionaryRequest;
import mx.geoint.Model.Dictionary.DictionaryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;

@CrossOrigin(origins = {"http://infokaab.com/","http://infokaab.com.mx/","http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009","http://10.2.102.182"})
@RestController
@RequestMapping(path="api/dictionary")
public class DictionaryController {
    private final DictionaryService dictionaryService;
    private Logger logger;

    public DictionaryController(DictionaryService dictionaryService){
        this.dictionaryService = dictionaryService;
        this.logger = new Logger();
    }

    @RequestMapping(path="/bases/list", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DictionaryResponse> listBases(@RequestBody DictionaryPaginate dictionaryPaginate) {
        try{
            DictionaryResponse dictionaryResponse = dictionaryService.getRegisters(dictionaryPaginate, "bases");
            return ResponseEntity.status(HttpStatus.OK).body(dictionaryResponse);
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SQLException", e);
        }
    }

    @RequestMapping(value={"/bases/delete/{id}"}, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public boolean deleteBases(@PathVariable int id)  {
        try{
            return dictionaryService.deleteRegister(id,"bases");
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SQLException", e);
        }
    }

    @RequestMapping(path = "/bases/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public boolean createRegisterBases(@RequestBody DictionaryRequest dictionaryRequest) {
        try{
            System.out.println("Create Register");
            return dictionaryService.insertRegister(dictionaryRequest, "bases");
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SQLException", e);
        }
    }

    @RequestMapping(path = "/bases/update", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public boolean updateRegisterBases(@RequestBody DictionaryRequest dictionaryRequest) {
        try{
            System.out.println("update Register");
            return dictionaryService.updateRegister(dictionaryRequest, "bases");
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SQLException", e);
        }
    }

    @RequestMapping(path="/prefixes/list", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DictionaryResponse> listPrefixes(@RequestBody DictionaryPaginate dictionaryPaginate) {
        try{
            DictionaryResponse dictionaryResponse = dictionaryService.getRegisters(dictionaryPaginate, "prefijos");
            return ResponseEntity.status(HttpStatus.OK).body(dictionaryResponse);
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SQLException", e);
        }
    }

    @RequestMapping(value={"/prefixes/delete/{id}"}, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public boolean deletePrefixes(@PathVariable int id) throws SQLException {
        return dictionaryService.deleteRegister(id, "prefijos");
    }

    @RequestMapping(path = "/prefixes/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public boolean createRegisterPrefixes(@RequestBody DictionaryRequest dictionaryRequest) {
        try{
            System.out.println("Create Register");
            return dictionaryService.insertRegister(dictionaryRequest, "prefijos");
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SQLException", e);
        }
    }

    @RequestMapping(path = "/prefixes/update", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public boolean updateRegisterPrefixes(@RequestBody DictionaryRequest dictionaryRequest) {
        try{
            System.out.println("update Register");
            return dictionaryService.updateRegister(dictionaryRequest, "prefijos");
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SQLException", e);
        }
    }
    @RequestMapping(path="/suffixes/list", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DictionaryResponse> listSuffixes(@RequestBody DictionaryPaginate dictionaryPaginate) {
        try{
            DictionaryResponse dictionaryResponse = dictionaryService.getRegisters(dictionaryPaginate, "sufijos");
            return ResponseEntity.status(HttpStatus.OK).body(dictionaryResponse);
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SQLException", e);
        }
    }

    @RequestMapping(value={"/suffixes/delete/{id}"}, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public boolean deleteSuffixes(@PathVariable int id) throws SQLException {
        return dictionaryService.deleteRegister(id, "sufijos");
    }

    @RequestMapping(path = "/suffixes/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public boolean createRegisterSuffixes(@RequestBody DictionaryRequest dictionaryRequest) {
        try{
            System.out.println("Create Register");
            return dictionaryService.insertRegister(dictionaryRequest, "sufijos");
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SQLException", e);
        }
    }

    @RequestMapping(path = "/suffixes/update", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public boolean updateRegisterSuffixes(@RequestBody DictionaryRequest dictionaryRequest) {
        try{
            System.out.println("update Register");
            return dictionaryService.updateRegister(dictionaryRequest, "sufijos");
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SQLException", e);
        }
    }
}
