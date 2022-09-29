package mx.geoint.Glosa.Dictionary;

import mx.geoint.Model.DictionaryDoc;
import mx.geoint.Response.DictionaryResponse;
import mx.geoint.Searcher.Search;
import mx.geoint.User.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009","http://10.2.102.182"})
@RestController
@RequestMapping(path="api/dictionary")
public class DictionaryController {
    private final DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService){
        this.dictionaryService = dictionaryService;
    }

    @RequestMapping(path="/bases/list", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DictionaryResponse> listBases(@RequestBody DictionaryPaginate dictionaryPaginate) throws SQLException {
        DictionaryResponse dictionaryResponse = dictionaryService.getRegisters(dictionaryPaginate, "bases");
        return ResponseEntity.status(HttpStatus.OK).body(dictionaryResponse);
    }

    @RequestMapping(value={"/bases/delete/{id}"}, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public boolean deleteBases(@PathVariable int id) throws SQLException {
        return dictionaryService.deleteRegister(id,"bases");
    }

    @RequestMapping(path = "/bases/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public boolean createRegisterBases(@RequestBody DictionaryRequest dictionaryRequest) throws SQLException {
        System.out.println("Create Register");
        return dictionaryService.insertRegister(dictionaryRequest, "bases");
    }

    @RequestMapping(path = "/bases/update", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public boolean updateRegisterBases(@RequestBody DictionaryRequest dictionaryRequest) throws SQLException {
        System.out.println("update Register");
        return dictionaryService.updateRegister(dictionaryRequest, "bases");
    }

    @RequestMapping(path="/prefixes/list", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DictionaryResponse> listPrefixes(@RequestBody DictionaryPaginate dictionaryPaginate) throws SQLException {
        DictionaryResponse dictionaryResponse = dictionaryService.getRegisters(dictionaryPaginate, "prefijos");
        return ResponseEntity.status(HttpStatus.OK).body(dictionaryResponse);
    }

    @RequestMapping(value={"/prefixes/delete/{id}"}, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public boolean deletePrefixes(@PathVariable int id) throws SQLException {
        return dictionaryService.deleteRegister(id, "prefijos");
    }

    @RequestMapping(path = "/prefixes/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public boolean createRegisterPrefixes(@RequestBody DictionaryRequest dictionaryRequest) throws SQLException {
        System.out.println("Create Register");
        return dictionaryService.insertRegister(dictionaryRequest, "prefijos");
    }

    @RequestMapping(path = "/prefixes/update", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public boolean updateRegisterPrefixes(@RequestBody DictionaryRequest dictionaryRequest) throws SQLException {
        System.out.println("update Register");
        return dictionaryService.updateRegister(dictionaryRequest, "prefijos");
    }
    @RequestMapping(path="/suffixes/list", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DictionaryResponse> listSuffixes(@RequestBody DictionaryPaginate dictionaryPaginate) throws SQLException {
        DictionaryResponse dictionaryResponse = dictionaryService.getRegisters(dictionaryPaginate, "sufijos");
        return ResponseEntity.status(HttpStatus.OK).body(dictionaryResponse);
    }

    @RequestMapping(value={"/suffixes/delete/{id}"}, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public boolean deleteSuffixes(@PathVariable int id) throws SQLException {
        return dictionaryService.deleteRegister(id, "sufijos");
    }

    @RequestMapping(path = "/suffixes/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public boolean createRegisterSuffixes(@RequestBody DictionaryRequest dictionaryRequest) throws SQLException {
        System.out.println("Create Register");
        return dictionaryService.insertRegister(dictionaryRequest, "sufijos");
    }

    @RequestMapping(path = "/suffixes/update", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public boolean updateRegisterSuffixes(@RequestBody DictionaryRequest dictionaryRequest) throws SQLException {
        System.out.println("update Register");
        return dictionaryService.updateRegister(dictionaryRequest, "sufijos");
    }
}
