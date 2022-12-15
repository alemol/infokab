package mx.geoint.Model.Report;

public class ReportRequest {
    private Integer id_proyecto;
    private String titulo;
    private String reporte;
    private String tipo;
    private String comentario;

    public ReportRequest(){}
    public ReportRequest(Integer id_proyecto, String titulo, String reporte){
        this.id_proyecto = id_proyecto;
        this.titulo = titulo;
        this.reporte = reporte;
    }

    public void setReporte(String reporte) { this.reporte = reporte; }

    public String getReporte() { return reporte; }

    public String getTitulo() { return titulo; }

    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Integer getId_proyecto() { return id_proyecto; }

    public void setId_proyecto(Integer id_proyecto) { this.id_proyecto = id_proyecto; }

    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getTipo() { return tipo;}

    public void setComentario(String comentario) { this.comentario = comentario; }

    public String getComentario() { return comentario; }
}
