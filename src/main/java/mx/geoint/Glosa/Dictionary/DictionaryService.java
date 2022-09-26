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

    public DictionaryResponse getList(int page, int recordsPerPage, String tableName) throws SQLException {
        return dbDictionary.ListRegistersDictionary(page, recordsPerPage, tableName);
    }
    public boolean deleteRegister(int register, String tableName) throws SQLException {
        return dbDictionary.deleteRegisterDictionary(register,tableName);
    }

    public boolean insertRegister(DictionaryRequest dictionaryRequest, String tableName) throws SQLException {
        return dbDictionary.insertRegisterDictionary(dictionaryRequest, tableName);
    }

    public boolean updateRegister(DictionaryRequest dictionaryRequest, String tableName) throws SQLException {
        return dbDictionary.updateRegisterDictionary(dictionaryRequest, tableName);
    }
}
