package mx.geoint.User;

public class User {
    private String nombre;
    private String apellido;
    private String correo;
    private String password;

    User(){
        nombre="";
        apellido="";
        correo="";
        password="";
    }

    public String getNombre(){
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPassword() {
        return password;
    }
}
