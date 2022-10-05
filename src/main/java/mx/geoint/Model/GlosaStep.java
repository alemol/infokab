package mx.geoint.Model;

import java.util.ArrayList;

public class GlosaStep {
    private int id;
    private String word;
    private ArrayList<String> parsing;
    public GlosaStep(int id, String word, ArrayList<String> parsing){
        this.id = id;
        this.word = word;
        this.parsing = parsing;
    }

    public void setId(int id) { this.id = id; }
    public int getId() { return id; }
    public void setWord(String word) { this.word = word; }
    public String getWord() { return word; }
    public void setId(ArrayList<String> parsing) { this.parsing = parsing; }
    public ArrayList<String> getParsing() { return parsing; }

}
