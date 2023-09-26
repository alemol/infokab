package mx.geoint.Model.Search;

import java.util.ArrayList;

public class SearchRequest {
    private String text;
    private String index;
    private ArrayList<String> cvegeo;

    private boolean levenshtein;

    private String id_usuario;

    public SearchRequest(){}
    public SearchRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setIndex(String index) { this.index = index; }

    public String getIndex() { return index; }

    public boolean isLevenshtein() {
        return levenshtein;
    }

    public void setLevenshtein(boolean levenshtein) {
        this.levenshtein = levenshtein;
    }

    public ArrayList<String> getCvegeo() {
        return cvegeo;
    }

    public void setCvegeo(ArrayList<String> cvegeo) {
        this.cvegeo = cvegeo;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }
}
