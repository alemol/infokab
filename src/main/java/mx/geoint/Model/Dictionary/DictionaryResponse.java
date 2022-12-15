package mx.geoint.Model.Dictionary;

import mx.geoint.Model.Dictionary.DictionaryDoc;

import java.util.ArrayList;

public class DictionaryResponse {
    private ArrayList<DictionaryDoc> registers;
    private long totalHits;

    public DictionaryResponse(ArrayList<DictionaryDoc> registers, long totalHits) {
        this.registers = registers;
        this.totalHits = totalHits;
    }

    public ArrayList<DictionaryDoc> getRegisters() {
        return registers;
    }

    public void setRegisters(ArrayList<DictionaryDoc> registers) { this.registers = registers; }

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }
}
