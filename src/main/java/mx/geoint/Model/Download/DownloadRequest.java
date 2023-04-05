package mx.geoint.Model.Download;

public class DownloadRequest {
    private String text;
    private String index;

    private String email;

    public DownloadRequest(){}

    public DownloadRequest(String text, String index) {
        this.text = text;
        this.index = index;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
