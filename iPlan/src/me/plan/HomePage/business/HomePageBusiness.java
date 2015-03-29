package me.plan.HomePage.business;

import android.net.Uri;
import android.os.Debug;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import me.plan.HomePage.data.NewPlanItemRsp;
import me.plan.HomePage.data.PlanListRsp;
import me.plan.HomePage.data.UploadPictureRsp;
import me.plan.comm.Utils;
import me.plan.comm.Widget.PredicateLayout;
import me.plan.comm.define.ParamDef;
import me.plan.core.Global;
import me.plan.core.LoginUser;
import me.plan.core.TLog;
import me.plan.core.UI.ToastUtil;
import me.plan.core.network.BlkNetWorker;
import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by tangb4c on 2015/2/3.
 */
public class HomePageBusiness {
    ObjectMapper objectMapper = new ObjectMapper();
    private static HomePageBusiness g = new HomePageBusiness();
    public static HomePageBusiness g(){
        return g;
    }
    public void appendNewPlan(final String planId, final String text, String filePath) throws FileNotFoundException {
        uploadPicture(filePath, new PictureUploadRspCallback() {
            @Override
            public void dealSuccResult(UploadPictureRsp rsp) {
                appendPlanItem(planId, rsp.data.id, text);
            }
        });
    }
    public void uploadPicture(String filePath, final PictureUploadRspCallback callback) throws FileNotFoundException {
        RequestParams requestParams = new RequestParams();
        requestParams.put("image", new File(filePath));
        BlkNetWorker.g().postPicture(ParamDef.CMD_UPLOAD_PIC, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
//                    Debug.waitForDebugger();
                    UploadPictureRsp rsp = Global.objectMapper.readValue(responseBody, UploadPictureRsp.class);
                    if (rsp.data == null || Utils.isEmpty(rsp.data.id)) {
                        ToastUtil.show(Global.getContext(), "服务器返回数据异常");
                        TLog.e("rsp.data %s", rsp.data);
                    } else if (callback != null) {
                        callback.dealSuccResult(rsp);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                TLog.e("拉取列表失败, statusCode:%d exception:%s", statusCode, error);
            }
        });
    }
    public void appendPlanItem(String planId, String picUrl, String text) {
        RequestParams requestParams = new RequestParams();
        requestParams.add(ParamDef.PICURL, picUrl);
        requestParams.add(ParamDef.PLANID, planId);
        requestParams.add(ParamDef.CONTENT, text);
        BlkNetWorker.g().get(ParamDef.CMD_APPEND_FEED, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
                TLog.e("添加新计划失败, statusCode:%d response:%s exception:%s", statusCode, responseString, throwable);
            }

            @Override
            public void onSuccess(final int statusCode, final Header[] headers, final String responseString) {
                try {
                    NewPlanItemRsp rsp = Global.objectMapper.readValue(responseString, NewPlanItemRsp.class);
                    if (rsp.data == null || rsp.data.id == null) {
                        ToastUtil.show(Global.getContext(), "添加新内容失败");
                    }else {
                        ToastUtil.show(Global.getContext(), "添加新内容成功");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private abstract class PictureUploadRspCallback {
        public abstract void dealSuccResult(final UploadPictureRsp rsp);
    }
}
