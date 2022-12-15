package mx.geoint.Model;

import com.google.gson.Gson;
import mx.geoint.Controllers.ParseXML.Tier;
import org.apache.commons.io.FilenameUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class SearchDoc {
    private String filePath;
    private String fileName;
    private String text;

    private float score;
    private String basePath;
    private String multimediaName;
    private String typePath;

    private String originalPath;

    private float startTime;

    private float endTime;

    public SearchDoc(String filePath, String fileName, String text, float score) throws FileNotFoundException {
        this.filePath = filePath;
        this.fileName = fileName;
        this.text = text;
        this.score = score;

        FileReader reader = new FileReader(filePath);
        Gson gson = new Gson();
        Tier tier = gson.fromJson(reader, Tier.class);

        basePath = FilenameUtils.getPath(tier.MEDIA_PATH).replace("./Files/","/");
        multimediaName = FilenameUtils.getBaseName(tier.MEDIA_PATH);
        typePath = FilenameUtils.getExtension(tier.MEDIA_PATH);

        originalPath = tier.ORIGINAL_MEDIA_PATH.replace("./Files/", "/");
        startTime = Float.parseFloat(tier.TIME_VALUE1) / 1000;
        endTime = Float.parseFloat(tier.TIME_VALUE2) / 1000;
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

    public String getMultimediaName(){ return multimediaName; }
    public String getBasePath(){ return basePath; }
    public String getTypePath(){ return typePath; }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public float getStartTime() {
        return startTime;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }

    public float getEndTime() {
        return endTime;
    }

    public void setEndTime(float endTime) {
        this.endTime = endTime;
    }
}
