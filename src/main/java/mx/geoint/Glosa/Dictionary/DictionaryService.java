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

    public DictionaryResponse getRegisters(DictionaryPaginate dictionaryPaginate, String tableName) throws SQLException {
        String search = dictionaryPaginate.getSearch();
        int page = dictionaryPaginate.getPage();
        int recordsPerPage = dictionaryPaginate.getRecord();

        int currentPage = (page - 1) * recordsPerPage;
        return dbDictionary.ListRegistersDictionary(currentPage, recordsPerPage, search, tableName);
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
