package mx.geoint.Model.Search;

public class SearchRequest {
    private String text;
    private String index;

    public SearchRequest(){}
    public SearchRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setIndex(String index) { this.index = index; }

    public String getIndex() { return index; }
}
