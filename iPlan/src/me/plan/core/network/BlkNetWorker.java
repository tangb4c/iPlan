package me.plan.core.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
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
    public void get(String relativeUrl, RequestParams params, TextHttpResponseHandler callback) {
        params.add("version", "2.2.2");
        client.get(requestUrl(relativeUrl), params, callback);
    }
    String requestUrl(String relativeUrl) {
        final String url = String.format("%s/%s", BASE_URL, relativeUrl);
        TLog.i(url);
        return url;
    }
}
