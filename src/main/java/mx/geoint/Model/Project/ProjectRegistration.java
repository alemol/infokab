package mx.geoint.Model.Project;

import java.sql.Timestamp;

public class ProjectRegistration {
    String id_proyecto;
    String id_usuario;
    String nombre_proyecto;
    String ruta_trabajo;
    String fecha_creacion;
    String estado;
    String fecha_archivo;
    String hablantes;
    String ubicacion;
    int radio;
    String bounds;

    int total_de_anotaciones;
    int anotaciones_guardadas;
    int total_de_reportes;

    public int lastIndex;
    public int totalImages;

    public String[] imageList;

    public int getLastIndex() {return lastIndex;}
    public void setLastIndex(int lastIndex){this.lastIndex = lastIndex;}

    public int getTotalImages() {return totalImages;}
    public void setTotalImages(int totalImages){this.totalImages = totalImages;}

    public String[] getimageList(){return imageList;}
    public void setimageList(String[] imageList) { this.imageList = imageList; }

    public String getId_proyecto() { return id_proyecto; }

    public void setId_proyecto(String id_proyecto) { this.id_proyecto = id_proyecto; }

    public String getId_usuario() { return id_usuario; }

    public void setId_usuario(String id_usuario) { this.id_usuario = id_usuario; }

    public String getNombre_proyecto() { return nombre_proyecto; }

    public void setNombre_proyecto(String nombre_proyecto) { this.nombre_proyecto = nombre_proyecto; }

    public String getRuta_trabajo() { return ruta_trabajo; }

    public void setRuta_trabajo(String ruta_trabajo) { this.ruta_trabajo = ruta_trabajo; }

    public String getFecha_creacion() { return fecha_creacion; }

    public void setFecha_creacion(String fecha_creacion) { this.fecha_creacion = fecha_creacion;}

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }

    public String getFecha_archivo() { return fecha_archivo; }

    public void setFecha_archivo(String fecha_archivo) { this.fecha_archivo = fecha_archivo; }

    public String getHablantes() { return hablantes; }

    public void setHablantes(String hablantes) { this.hablantes = hablantes; }

    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getUbicacion() { return ubicacion; }

    public int getRadio() { return radio; }

    public void setRadio(int radio) { this.radio = radio; }

    public String getBounds() { return bounds; }

    public void setBounds(String bounds) { this.bounds = bounds; }

    public int getAnotaciones_guardadas() { return anotaciones_guardadas; }

    public void setAnotaciones_guardadas(int anotaciones_guardadas) { this.anotaciones_guardadas = anotaciones_guardadas; }

    public int getTotal_de_anotaciones() { return total_de_anotaciones; }

    public void setTotal_de_anotaciones(int total_de_anotaciones) { this.total_de_anotaciones = total_de_anotaciones; }

    public void setTotal_de_reportes(int total_de_reportes) { this.total_de_reportes = total_de_reportes; }

    public int getTotal_de_reportes() { return total_de_reportes; }
}
