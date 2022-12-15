package mx.geoint.Model.Search;

public class SearchPage {
    private String text;
    private int page;

    public SearchPage(){}

    public SearchPage(String text, int page) {
        this.text = text;
        this.page = page;
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
}
