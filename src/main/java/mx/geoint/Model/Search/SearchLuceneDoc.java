package mx.geoint.Model.Search;

import com.google.gson.Gson;
import mx.geoint.Model.ParseXML.Tier;
import org.apache.commons.io.FilenameUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class SearchLuceneDoc {
    private String Nhablantes;
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

    private String[] imageList;

    private String fecha_archivo;

    private String entidad;

    private String municipio;

    public SearchLuceneDoc(String filePath, String fileName, String text, float score, String[] imageList, String fecha_archivo, String Nhablantes, String entidad, String municipio) throws FileNotFoundException {
        this.filePath = filePath;
        this.fileName = fileName;
        this.text = text;
        this.score = score;

        //FileReader reader = new FileReader(filePath);
        //Gson gson = new Gson();
        //Tier tier = gson.fromJson(reader, Tier.class);

        basePath = FilenameUtils.getPath(multimedia).replace("./Files/","/");
        multimediaName = FilenameUtils.getBaseName(multimedia);
        typePath = FilenameUtils.getExtension(multimedia);

        //originalPath = tier.ORIGINAL_MEDIA_PATH.replace("./Files/", "/");
        //startTime = Float.parseFloat(tier.TIME_VALUE1) / 1000;
        //endTime = Float.parseFloat(tier.TIME_VALUE2) / 1000;

        this.imageList = imageList;
        this.fecha_archivo = fecha_archivo;
        this.Nhablantes = Nhablantes;
        this.entidad = entidad;
        this.municipio = municipio;
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

    public String[] getimageList(){return imageList;}
    public void setimageList(String[] imageList) { this.imageList = imageList; }

    public String getFecha_archivo(){ return fecha_archivo; }
    public void  setFecha_archivo(String fecha_archivo){this.fecha_archivo = fecha_archivo;}

    public String getNhablantes(){ return Nhablantes; }
    public void  setNhablantes(String Nhablantes){this.Nhablantes = Nhablantes;}

    public String getEntidad(){ return entidad; }
    public void  setEntidad(String entidad){this.entidad = entidad;}


    public String getMunicipio(){ return municipio; }
    public void  setMunicipio(String municipio){this.municipio = municipio;}
}
