package mx.geoint.Model.Glosado;

import mx.geoint.Model.Glosado.GlosaStep;

import java.util.ArrayList;

public class Glosa {
    private int id;
    private String word;
    private ArrayList<GlosaStep> steps;

    public Glosa(int id, String word, ArrayList<GlosaStep> steps) {
        this.id = id;
        this.word = word;
        this.steps = steps;
    }

    public float getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public ArrayList<GlosaStep> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<GlosaStep> steps) {
        this.steps = steps;
    }
}
