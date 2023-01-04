package mx.geoint.Model.Search;

import java.util.ArrayList;

public class SearchResponse {

    private ArrayList<SearchLuceneDoc> documents;
    private long totalHits;

    public SearchResponse(ArrayList<SearchLuceneDoc> documents, long totalHits) {
        this.documents = documents;
        this.totalHits = totalHits;
    }

    public ArrayList<SearchLuceneDoc> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<SearchLuceneDoc> documents) {
        this.documents = documents;
    }

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }
}
