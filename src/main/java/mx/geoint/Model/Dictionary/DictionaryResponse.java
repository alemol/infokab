package mx.geoint.Model.Dictionary;

import java.util.ArrayList;

public class DictionaryResponse {
    private ArrayList<DictionaryPostgresRegister> registers;
    private long totalHits;

    public DictionaryResponse(ArrayList<DictionaryPostgresRegister> registers, long totalHits) {
        this.registers = registers;
        this.totalHits = totalHits;
    }

    public ArrayList<DictionaryPostgresRegister> getRegisters() {
        return registers;
    }

    public void setRegisters(ArrayList<DictionaryPostgresRegister> registers) { this.registers = registers; }

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }
}
