package mx.geoint.Model;

public class ReportRequest {
    private Integer id_proyecto;
    private String titulo;
    private String reporte;

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
}
