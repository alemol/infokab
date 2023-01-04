package mx.geoint.Apis.Report;

import mx.geoint.Controllers.Logger.Logger;
import mx.geoint.Model.General.GeneralPaginateResponse;
import mx.geoint.Model.Report.ReportRequest;
import mx.geoint.Model.Report.ReportsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;

@CrossOrigin(origins = {"http://infokaab.com/","http://infokaab.com.mx/","http://localhost:3009", "http://localhost:3000", "http://10.2.102.182:3009","http://10.2.102.182"})
@RestController
@RequestMapping(path="api/report")
public class ReportController {
    private final ReportService reportService;
    private Logger logger;

    public ReportController(ReportService reportService){
        this.reportService = reportService;
        this.logger = new Logger();
    }

    /**
     *
     * @param generalPaginateResponse
     * @return
     */
    @RequestMapping(path="/registers", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ReportsResponse> getReports(@RequestBody GeneralPaginateResponse generalPaginateResponse) {
        try{
            ReportsResponse reportsResponse = reportService.getRegisters(generalPaginateResponse);
            return ResponseEntity.status(HttpStatus.OK).body(reportsResponse);
        } catch (SQLException e) {
            logger.appendToFile(e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "SQLException", e);
        }
    }

    @RequestMapping(path="/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public boolean createRegisterBases(@RequestBody ReportRequest reportRequest) throws SQLException {
        return reportService.insertRegister(reportRequest);
    }
}
