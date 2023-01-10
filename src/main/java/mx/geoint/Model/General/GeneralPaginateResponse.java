package mx.geoint.Model.General;

public class GeneralPaginateResponse {
    private int page;
    private int record;
    private String search;
    private Integer id;

    public GeneralPaginateResponse(){
        this.page = 1;
        this.record = 10;
    }
    public GeneralPaginateResponse(int page, int record, String search){
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
