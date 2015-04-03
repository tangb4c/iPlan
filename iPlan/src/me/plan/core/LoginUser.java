package me.plan.core;

/**
 * 当前登录用户信息
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


    /**
     * 用户登录ID（后台对应是UID）
     */
    private String userId;

    //登录态，会有过期的情况
    private String ukey;

    public boolean isHasLogined() {
        return hasLogined;
    }

    public void setHasLogined(final boolean hasLogined) {
        this.hasLogined = hasLogined;
    }

    private boolean hasLogined = false;
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUkey() {
        return ukey;
    }

    public void setUkey(String ukey) {
        this.ukey = ukey;
    }


}
