package mx.geoint.Apis.Report;

import mx.geoint.Database.DBAnnotations;
import mx.geoint.Database.DBDictionary;
import mx.geoint.Database.DBProjects;
import mx.geoint.Database.DBReports;
import mx.geoint.Model.General.GeneralPaginateResponse;
import mx.geoint.Model.Report.ReportRequest;
import mx.geoint.Model.Report.ReportsResponse;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class ReportService {
    public static DBReports dbReports;

    public ReportService(){
        this.dbReports = new DBReports();
    }

    public ReportsResponse getRegisters(GeneralPaginateResponse generalPaginateResponse) throws SQLException {
        int page = generalPaginateResponse.getPage();
        Integer id_project = generalPaginateResponse.getId();
        int recordsPerPage = generalPaginateResponse.getRecord();
        String search = generalPaginateResponse.getSearch();

        int currentPage = (page - 1) * recordsPerPage;
        return dbReports.ListRegisters(currentPage, recordsPerPage, id_project, search);
    }

    public boolean insertRegister(ReportRequest reportRequest) throws SQLException {
        int id_project = reportRequest.getId_proyecto();
        String title = reportRequest.getTitulo();
        String report = reportRequest.getReporte();
        String tipo = reportRequest.getTipo();
        String comentario = reportRequest.getComentario();

        return dbReports.newRegister(id_project, title, report, tipo, comentario, "");
    }

}
