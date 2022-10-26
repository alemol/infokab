package mx.geoint.Glosa.Dictionary;

public class DictionaryPaginate {
    private int page;
    private int record;
    private String search;
    private Integer id;

    public DictionaryPaginate(){
        this.page = 1;
        this.record = 10;
    }
    public DictionaryPaginate(int page, int record, String search){
        if(page <= 0){
            this.page = 1;
        }else{
            this.page = page;
        }

        if(page <= 0){
            this.record = 10;
        }else{
            this.record = record;
        }

        this.search = search;
    }

    public int getPage(){ return page; }
    public int getRecord(){ return record; }
    public String getSearch(){ return search; }
    public void setId(Integer id) { this.id = id; }
    public Integer getId() { return id; }
}
