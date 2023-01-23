package mx.geoint.Model.Search;

public class SearchPage {
    private String text;
    private String index;
    private int page;

    public SearchPage(){}

    public SearchPage(String text, int page, String index) {
        this.text = text;
        this.page = page;
        this.index = index;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setIndex(String index) { this.index = index; }

    public String getIndex() { return index; }
}
