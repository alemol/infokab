package mx.geoint.User;

public class User {
    private String uuid;
    private String nombre;
    private String apellido;
    private String correo;
    private String password;
    private String permisos;

    public User() {
        uuid = "";
        nombre = "";
        apellido = "";
        correo = "";
        password = "";
        permisos = "{}";
    }
    public String getUUID() { return uuid; }
    public void setUUID(String uuid) { this.uuid = uuid; }
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {this.apellido = apellido;}
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {this.correo = correo;}
    public String getPassword() {
        return password;
    }

    public String getPermisos() {return permisos;}
    public void setPermisos(String permisos) {this.correo = permisos;}
}
