package mx.geoint.Glosa.Dictionary;

import mx.geoint.Model.DictionaryDoc;
import mx.geoint.Response.DictionaryResponse;
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
    public ResponseEntity<DictionaryResponse> list(@PathVariable Optional<Integer> selectPage) throws SQLException {
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

        DictionaryResponse dictionaryResponse = dictionaryService.getList(currentPage, recordsPerPage);
        return ResponseEntity.status(HttpStatus.OK).body(dictionaryResponse);
    }

}
