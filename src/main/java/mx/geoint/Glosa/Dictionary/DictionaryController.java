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

@CrossOrigin(origins = {"http://localhost:3000", "http://10.2.102.202:3000/","http://10.2.102.182"})
@RestController
@RequestMapping(path="api/dictionary")
public class DictionaryController {
    private final DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService){
        this.dictionaryService = dictionaryService;
    }

    @RequestMapping(value={"/bases/list/", "/bases/list/{selectPage}"}, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DictionaryResponse> listBases(@PathVariable Optional<Integer> selectPage) throws SQLException {
        int page = 1;
        int recordsPerPage = 10;
        if (selectPage.isPresent()) {
            if(selectPage.get() <= 0){
                page = 1;
            } else{
                page = selectPage.get();
            }
        }

        int currentPage = (page - 1) * recordsPerPage;

        DictionaryResponse dictionaryResponse = dictionaryService.getList(currentPage, recordsPerPage, "bases");
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

    @RequestMapping(value={"/prefixes/list/", "/prefixes/list/{selectPage}"}, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DictionaryResponse> listPrefixes(@PathVariable Optional<Integer> selectPage) throws SQLException {
        int page = 1;
        int recordsPerPage = 10;
        if (selectPage.isPresent()) {
            if(selectPage.get() <= 0){
                page = 1;
            } else{
                page = selectPage.get();
            }
        }

        int currentPage = (page - 1) * recordsPerPage;

        DictionaryResponse dictionaryResponse = dictionaryService.getList(currentPage, recordsPerPage, "prefijos");
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

    @RequestMapping(value={"/suffixes/list/", "/suffixes/list/{selectPage}"}, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DictionaryResponse> listSuffixes(@PathVariable Optional<Integer> selectPage) throws SQLException {
        int page = 1;
        int recordsPerPage = 10;
        if (selectPage.isPresent()) {
            if(selectPage.get() <= 0){
                page = 1;
            } else{
                page = selectPage.get();
            }
        }

        int currentPage = (page - 1) * recordsPerPage;

        DictionaryResponse dictionaryResponse = dictionaryService.getList(currentPage, recordsPerPage, "sufijos");
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
}
