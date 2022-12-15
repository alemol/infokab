package mx.geoint.Response;

import mx.geoint.Model.Report.ReportDoc;

import java.util.ArrayList;

public class ReportsResponse {
    private ArrayList<ReportDoc> registers;
    private long totalHits;

    public ReportsResponse(ArrayList<ReportDoc> registers, long totalHits) {
        this.registers = registers;
        this.totalHits = totalHits;
    }

    public void setRegisters(ArrayList<ReportDoc> registers) { this.registers = registers; }

    public ArrayList<ReportDoc> getRegisters() { return registers; }

    public void setTotalHits(long totalHits) { this.totalHits = totalHits; }

    public long getTotalHits() { return totalHits; }
}
