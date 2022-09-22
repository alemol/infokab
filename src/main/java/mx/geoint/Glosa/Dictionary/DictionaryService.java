package mx.geoint.Glosa.Dictionary;

import mx.geoint.Model.DictionaryDoc;
import mx.geoint.Response.DictionaryResponse;
import mx.geoint.database.DBDictionary;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class DictionaryService {
    public static DBDictionary dbDictionary;

    public DictionaryService(){
        this.dbDictionary = new DBDictionary();
    }

    public DictionaryResponse getList(int page, int recordsPerPage) throws SQLException {
        return dbDictionary.ListBases(page, recordsPerPage);
    }
}
