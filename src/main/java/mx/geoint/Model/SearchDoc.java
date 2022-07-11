package mx.geoint.Model;

public class SearchDoc {
    private String filePath;
    private String fileName;
    private String text;

    private float score;

    public SearchDoc(String filePath, String fileName, String text, float score) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.text = text;
        this.score = score;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
