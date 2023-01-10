package mx.geoint.Model.Report;

public class ReportPostgresRegister {
    String id;
    String id_proyecto;
    String titulo;
    String reporte;
    String fecha_creacion;
    String tipo;
    Boolean activate;
    String comentario;
    String anotacion;

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

    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getTipo() { return tipo; }

    public void setActivate(Boolean activate) { this.activate = activate;}

    public Boolean getActivate() { return activate; }

    public void setComentario(String comentario) { this.comentario = comentario; }

    public String getComentario() { return comentario; }

    public void setAnotacion(String anotacion) { this.anotacion = anotacion; }

    public String getAnotacion() { return anotacion; }
}
