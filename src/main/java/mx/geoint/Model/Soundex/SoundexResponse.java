package mx.geoint.Model.Soundex;

public class SoundexResponse {
    String originalWord;
    String PriorClass3;
    String PriorClass2;
    String PriorClass1;
    String scrap;
    String code;

    public void setOriginalWord(String originalWord) {
        this.originalWord = originalWord;
    }

    public String getOriginalWord() {
        return originalWord;
    }

    public String getPriorClass1() {
        return PriorClass1;
    }

    public void setPriorClass1(String priorClass1) {
        PriorClass1 = priorClass1;
    }

    public String getPriorClass2() {
        return PriorClass2;
    }

    public void setPriorClass2(String priorClass2) {
        PriorClass2 = priorClass2;
    }

    public String getPriorClass3() {
        return PriorClass3;
    }

    public void setPriorClass3(String priorClass3) {
        PriorClass3 = priorClass3;
    }

    public String getScrap() {
        return scrap;
    }

    public void setScrap(String scrap) {
        this.scrap = scrap;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

