package mx.geoint.Apis.Dictionary;

public class DictionaryRequest {
    private int id;
    private String clave;
    private String code;
    private String description;
    private String traduction;
    private String extra;

    public DictionaryRequest(){}
    public DictionaryRequest(String clave, String code, String description, String traduction, String extra) {
        this.clave = clave;
        this.code = code;
        this.description = description;
        this.traduction = traduction;
        this.extra = extra;
    }

    public DictionaryRequest(String clave, String code, String description, String traduction, String extra, int id) {
        this.id = id;
        this.clave = clave;
        this.code = code;
        this.description = description;
        this.traduction = traduction;
        this.extra = extra;
    }

    public int getId() { return id;}
    public String getClave() { return clave;}
    public String getCode() { return code;}
    public String getDescription() { return description;}
    public String getTraduction() { return traduction;}
    public String getExtra() { return extra;}
}
