package mx.geoint.Model.User;

public class UserListResponse {
    String uuid;
    String nombres;
    String apellidos;
    String correo;
    String permisos;

    Integer id_rol;

    public String getUUID() { return uuid; }
    public void setUUID(String uuid) { this.uuid = uuid; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getPermisos() { return permisos; }
    public void setPermisos(String permisos) { this.permisos = permisos; }

    public Integer getId_rol() { return id_rol; }
    public void setId_rol(Integer id_rol) { this.id_rol = id_rol; }

}
