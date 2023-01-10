package mx.geoint.Model.Report;

import java.util.ArrayList;

public class ReportsResponse {
    private ArrayList<ReportPostgresRegister> registers;
    private long totalHits;

    public ReportsResponse(ArrayList<ReportPostgresRegister> registers, long totalHits) {
        this.registers = registers;
        this.totalHits = totalHits;
    }

    public void setRegisters(ArrayList<ReportPostgresRegister> registers) { this.registers = registers; }

    public ArrayList<ReportPostgresRegister> getRegisters() { return registers; }

    public void setTotalHits(long totalHits) { this.totalHits = totalHits; }

    public long getTotalHits() { return totalHits; }
}
