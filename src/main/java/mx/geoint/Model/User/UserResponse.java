package mx.geoint.Model.User;

import java.util.ArrayList;

public class UserResponse {
    private ArrayList<UserListResponse> users;
    private long totalHits;

    public UserResponse(ArrayList<UserListResponse> users, long totalHits) {
        this.users = users;
        this.totalHits = totalHits;
    }

    public ArrayList<UserListResponse> getRegisters() {
        return users;
    }

    public void setRegisters(ArrayList<UserListResponse> registers) { this.users = users; }

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }
}
