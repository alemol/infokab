package mx.geoint.Model.Search;

public class SearchRequest {
    private String text;

    public SearchRequest(){}
    public SearchRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
