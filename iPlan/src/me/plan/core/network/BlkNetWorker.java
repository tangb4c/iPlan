package me.plan.core.network;

import android.net.Uri;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;
import me.plan.comm.define.ParamDef;
import me.plan.core.Global;
import me.plan.core.LoginUser;
import me.plan.core.TLog;
import org.apache.http.entity.InputStreamEntity;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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

    public static final String BASE_URL = "http://182.254.167.228";
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
    public void uploadPicture(String relativeUrl, String filePath, AsyncHttpResponseHandler callback) throws FileNotFoundException, NullPointerException {
        final String MIME = "application/octet-stream";
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        InputStreamEntity inputStreamEntity = new InputStreamEntity(inputStream, file.length());
        RequestParams params = new RequestParams();
        //公用参数，后台检查
        params.add(ParamDef.VERSION, ParamDef.VERSIONCODE);
        params.add(ParamDef.LOGIN_TYPE, "uid");
        params.add(ParamDef.UID, LoginUser.g().getUserId());
        params.add(ParamDef.UKEY, LoginUser.g().getUkey());
        String reqUrl = requestUrl(relativeUrl);
        String reqUrlParams = String.format("%s?%s", reqUrl, params.toString());
        TLog.i("post request:%s", reqUrlParams);
        client.post(Global.getContext(), reqUrlParams, inputStreamEntity, MIME, callback);
    }
    public static String getImageUrl(final String imageId) {
        RequestParams params = new RequestParams();
        //公用参数，后台检查
        params.add(ParamDef.VERSION, ParamDef.VERSIONCODE);
        params.add(ParamDef.LOGIN_TYPE, "uid");
        params.add(ParamDef.UID, LoginUser.g().getUserId());
        params.add(ParamDef.UKEY, LoginUser.g().getUkey());
        params.add(ParamDef.ID, imageId);
        String url = String.format("%s/superplan/pic/splan_pic_get.php?%s", BASE_URL, params.toString());
        return url;
    }
}
