package me.plan.core;

/**
 * Created by tangb4c on 2015/2/1.
 */
public class LoginUser {
    private LoginUser() {
        userId = "100002";
    }
    private static final LoginUser g = new LoginUser();
    public static LoginUser g(){
        return g;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;

}
