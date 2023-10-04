package mx.geoint.Model.Search;

public class SearchPostgres {
    String id_busqueda;
    String id_usuario;
    String consulta;
    String fecha_creacion;
    String indice;

    String soundex;

    public String getId_busqueda() {
        return id_busqueda;
    }

    public void setId_busqueda(String id_busqueda) {
        this.id_busqueda = id_busqueda;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getConsulta() {
        return consulta;
    }

    public void setConsulta(String consulta) {
        this.consulta = consulta;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public String getSoundex() { return soundex; }

    public void setSoundex(String soundex) { this.soundex = soundex; }
}
