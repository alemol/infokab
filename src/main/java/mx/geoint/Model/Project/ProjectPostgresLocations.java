package mx.geoint.Model.Project;

public class ProjectPostgresLocations {
    private String localidad_cvegeo;
    private String localidad_nombre;
    private String municipio_cvegeo;
    private String bbox;
    private String geometria;

    public String getBbox() { return bbox; }

    public void setBbox(String bbox) { this.bbox = bbox; }

    public String getLocalidad_cvegeo() { return localidad_cvegeo; }

    public void setLocalidad_cvegeo(String localidad_cvegeo) { this.localidad_cvegeo = localidad_cvegeo; }

    public String getLocalidad_nombre() { return localidad_nombre; }

    public void setLocalidad_nombre(String localidad_nombre) { this.localidad_nombre = localidad_nombre; }

    public String getMunicipio_cvegeo() { return municipio_cvegeo; }

    public void setMunicipio_cvegeo(String municipio_cvegeo) { this.municipio_cvegeo = municipio_cvegeo; }

    public String getGeometria() { return geometria; }

    public void setGeometria(String geometria) { this.geometria = geometria; }
}
