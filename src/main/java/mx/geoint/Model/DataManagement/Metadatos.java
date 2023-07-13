package mx.geoint.Model.DataManagement;

public class Metadatos {
    String ruta_trabajo;
    String duration;
    String tamaño;
    String channels;
    String mime_type;
    String metadata;
    long folderSize;

    public String getRuta_trabajo(){return  ruta_trabajo;}
    public void setRuta_trabajo(String ruta_trabajo) {this.ruta_trabajo = ruta_trabajo; }

    public String getDuration(){return  duration;}
    public void setDuration(String duration) {this.duration = duration; }

    public String getTamaño(){return  tamaño;}
    public void setTamaño(String tamaño) {this.tamaño = tamaño; }

    public String getChannels(){return  channels;}
    public void setChannels(String channels) {this.channels = channels; }

    public String getMime_type(){return  mime_type;}
    public void setMime_type(String mime_type) {this.mime_type = mime_type; }

    public String getMetadata(){return  metadata;}
    public void setMetadata(String metadata) {this.metadata = metadata; }

    public long getFolderSize(){return  folderSize;}
    public void setFolderSize(long folderSize) {this.folderSize = folderSize; }
}
