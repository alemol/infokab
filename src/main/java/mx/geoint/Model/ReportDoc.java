package mx.geoint.Model;

public class ReportDoc {
    String id;
    String id_proyecto;
    String titulo;
    String reporte;
    String fecha_creacion;

    public void setId(String id) { this.id = id; }

    public String getId() { return id; }

    public void setId_proyecto(String id_proyecto) { this.id_proyecto = id_proyecto; }

    public String getId_proyecto() { return id_proyecto; }

    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getTitulo() { return titulo; }

    public void setReporte(String reporte) { this.reporte = reporte; }

    public String getReporte() { return reporte; }

    public void setFecha_creacion(String fecha_creacion) { this.fecha_creacion = fecha_creacion; }

    public String getFecha_creacion() { return fecha_creacion; }
}