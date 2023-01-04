package mx.geoint.Apis.Dictionary;

import mx.geoint.Model.General.GeneralPaginateResponse;
import mx.geoint.Model.Dictionary.DictionaryRequest;
import mx.geoint.Model.Dictionary.DictionaryResponse;
import mx.geoint.Database.DBDictionary;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class DictionaryService {
    public static DBDictionary dbDictionary;

    public DictionaryService(){
        this.dbDictionary = new DBDictionary();
    }

    public DictionaryResponse getRegisters(GeneralPaginateResponse generalPaginateResponse, String tableName) throws SQLException {
        String search = generalPaginateResponse.getSearch();
        int page = generalPaginateResponse.getPage();
        int recordsPerPage = generalPaginateResponse.getRecord();

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
