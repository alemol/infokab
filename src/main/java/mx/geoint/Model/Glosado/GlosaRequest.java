package mx.geoint.Model.Glosado;

import java.util.ArrayList;
import java.util.List;

public class GlosaRequest {
    private static ArrayList<String> annotations;

    public GlosaRequest(){}
    public GlosaRequest(ArrayList<String> annotations){
        this.annotations = annotations;
    }

    public void setAnnotations(ArrayList<String> annotations){this.annotations = annotations;}
    public static ArrayList<String> getAnnotations(){return annotations; }
}
