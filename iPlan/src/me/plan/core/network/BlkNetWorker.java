package me.plan.core.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;
import me.plan.comm.define.ParamDef;
import me.plan.core.LoginUser;
import me.plan.core.TLog;
import org.json.JSONObject;

/**
 * Created by tangb4c on 2015/1/31.
 */
public class BlkNetWorker {
    private BlkNetWorker(){

    }
    final static BlkNetWorker g = new BlkNetWorker();
    public static BlkNetWorker g(){
        return g;
    }

    final String BASE_URL = "http://182.254.167.228";
    AsyncHttpClient client = new AsyncHttpClient();
    String requestUrl(String relativeUrl) {
        final String url = String.format("%s/%s", BASE_URL, relativeUrl);
        TLog.i(url);
        return url;
    }

    public void get(String relativeUrl, RequestParams params, TextHttpResponseHandler callback) {
        //公用参数，后台检查
        params.add(ParamDef.VERSION, ParamDef.VERSIONCODE);
        params.add(ParamDef.LOGIN_TYPE, "uid");
        params.add(ParamDef.UID, LoginUser.g().getUserId());
        params.add(ParamDef.UKEY, LoginUser.g().getUkey());
        String reqUrl = requestUrl(relativeUrl);
        TLog.i("request:%s?%s", reqUrl, params.toString());
        client.get(requestUrl(relativeUrl), params, callback);
    }
    public void get_with_notoken(String relativeUrl, RequestParams params, TextHttpResponseHandler callback) {
        params.add(ParamDef.VERSION, ParamDef.VERSIONCODE);
        String reqUrl = requestUrl(relativeUrl);
        TLog.i("request:%s?%s", reqUrl, params.toString());
        client.get(requestUrl(relativeUrl), params, callback);
    }

    public void postPicture(String relativeUrl, RequestParams params, AsyncHttpResponseHandler callback) {
        String reqUrl = requestUrl(relativeUrl);
        TLog.i("post request:%s", reqUrl);
        client.post(reqUrl, params, callback);
    }
}
