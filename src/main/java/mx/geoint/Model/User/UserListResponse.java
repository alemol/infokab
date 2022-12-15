package mx.geoint.Model.User;

import mx.geoint.Model.User.UsersList;

import java.util.ArrayList;

public class UserListResponse {
    private ArrayList<UsersList> users;
    private long totalHits;

    public UserListResponse(ArrayList<UsersList> users, long totalHits) {
        this.users = users;
        this.totalHits = totalHits;
    }

    public ArrayList<UsersList> getRegisters() {
        return users;
    }

    public void setRegisters(ArrayList<UsersList> registers) { this.users = users; }

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }
}
