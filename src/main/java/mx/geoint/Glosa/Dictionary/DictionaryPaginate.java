package mx.geoint.Glosa.Dictionary;

public class DictionaryPaginate {
    private int page;
    private int record;
    private String search;

    public DictionaryPaginate(){}
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
}
