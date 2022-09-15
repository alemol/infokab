package mx.geoint.Model;

public class Glosa {
    private int id;
    private String word;
    private Object steps;

    public Glosa(int id, String word, Object steps) {
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

    public Object getSteps() {
        return steps;
    }

    public void setSteps(Object steps) {
        this.steps = steps;
    }
}
