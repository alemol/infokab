package mx.geoint.Model.Glosado;

import java.util.ArrayList;

public class GlosaStep {
    private int id;
    private String word;
    private String originalWord;
    private ArrayList<String> parsing;
    private String select = "";

    public GlosaStep(){ }
    public GlosaStep(int id, String originalWord, String word, ArrayList<String> parsing){
        this.id = id;
        this.originalWord = originalWord;
        this.word = word;
        this.parsing = parsing;
    }

    public GlosaStep(int id, String originalWord, String word, ArrayList<String> parsing, String select){
        this.id = id;
        this.originalWord = originalWord;
        this.word = word;
        this.parsing = parsing;
        this.select = select;
    }

    public void setId(int id) { this.id = id; }
    public int getId() { return id; }
    public void setWord(String word) { this.word = word; }
    public String getWord() { return word; }
    public void setParsing(ArrayList<String> parsing) { this.parsing = parsing; }
    public ArrayList<String> getParsing() { return parsing; }

    public void setSelect(String select) { this.select = select; }
    public String getSelect() { return select; }

    public void setOriginalWord(String originalWord) { this.originalWord = originalWord; }
    public String getOriginalWord() { return originalWord; }
}
