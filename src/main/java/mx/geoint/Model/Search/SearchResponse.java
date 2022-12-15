package mx.geoint.Model.Search;

import java.util.ArrayList;

public class SearchResponse {

    private ArrayList<SearchDoc> documents;
    private long totalHits;

    public SearchResponse(ArrayList<SearchDoc> documents, long totalHits) {
        this.documents = documents;
        this.totalHits = totalHits;
    }

    public ArrayList<SearchDoc> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<SearchDoc> documents) {
        this.documents = documents;
    }

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }
}
