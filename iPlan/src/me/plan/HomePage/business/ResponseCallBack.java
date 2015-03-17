package me.plan.HomePage.business;

/**
 * Created by tangb4c on 2015/2/3.
 */
public interface ResponseCallBack {
    void onFailure(final int statusCode, final String responseString, final Throwable throwable);
    void onSuccess(final int statusCode, final String responseString);
}
