package me.plan.HomePage.business;

import android.net.Uri;
import android.os.Debug;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import me.plan.HomePage.data.BaseRsp;
import me.plan.HomePage.data.NewPlanItemRsp;
import me.plan.HomePage.data.PlanListRsp;
import me.plan.HomePage.data.UploadPictureRsp;
import me.plan.HomePage.data.element.FeedItem;
import me.plan.HomePage.data.element.PlanInfo;
import me.plan.comm.Utils;
import me.plan.comm.Widget.PredicateLayout;
import me.plan.comm.busi.OperationCallback;
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
    public void appendNewPlan(final String planId, final String text, String filePath, final OperationCallback<FeedItem> callback) throws FileNotFoundException {
        uploadPicture(filePath, new PictureUploadRspCallback() {
            @Override
            public void dealSuccResult(UploadPictureRsp rsp) {
                appendPlanItem(planId, rsp.data.id, text, callback);
            }
        });
    }
    public void uploadPicture(String filePath, final PictureUploadRspCallback callback) throws FileNotFoundException {
        RequestParams requestParams = new RequestParams();
        BlkNetWorker.g().uploadPicture(ParamDef.CMD_UPLOAD_PIC, filePath, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
//                    Debug.waitForDebugger();
                    String rspText = TextHttpResponseHandler.getResponseString(responseBody, getCharset());
                    TLog.i("onSuccess. rspText:%s", rspText);
                    UploadPictureRsp rsp = Global.objectMapper.readValue(rspText, UploadPictureRsp.class);
                    if (rsp.data == null || Utils.isEmpty(rsp.data.id)) {
                        ToastUtil.show(Global.getContext(), "服务器返回数据异常");
                        TLog.e("rsp.data %s %s", rspText, rsp.data);
                    } else if (callback != null) {
                        callback.dealSuccResult(rsp);
                    }

                } catch (Exception e) {
                    TLog.e("onSuccess but error!%s", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                TLog.e("uploadPicture failed, statusCode:%d exception:%s", statusCode, error);
            }
        });
    }
    public void appendPlanItem(final String planId, final String picUrl, final String text, final OperationCallback<FeedItem> callback) {
        TLog.i("planId:%s url:%s text:%s", planId, picUrl, text);
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
                    TLog.i("appendPlanItem rsp:%s", responseString);
                    NewPlanItemRsp rsp = Global.objectMapper.readValue(responseString, NewPlanItemRsp.class);
                    if (rsp.data == null || rsp.data.id == null) {
                        ToastUtil.show(Global.getContext(), "添加新内容失败");
                        TLog.i("failed.");
                    } else {
                        ToastUtil.show(Global.getContext(), "添加新内容成功");
                        TLog.i("succ");
                        if (callback != null) {
                            FeedItem feedItem = new FeedItem();
                            feedItem.id = rsp.data.id;
                            feedItem.content = text;
                            feedItem.picurl = picUrl;
                            feedItem.createTime = Utils.getTimeStamp();
                            callback.onSucc(feedItem);
                        }
                    }
                } catch (Exception e) {
                    TLog.e("error happend.%s", e);
                }
            }
        });
    }
    public void changePlanState(final PlanInfo planInfo, final int state, final OperationCallback<BaseRsp> callback){
        RequestParams requestParams = new RequestParams();
        requestParams.add(ParamDef.ID, planInfo.id);
        requestParams.put(ParamDef.STATE, state);
        BlkNetWorker.g().get(ParamDef.CMD_SET_PLAN_STATE, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ToastUtil.show(Global.getContext(), "修改失败");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString)  {
                String text;
                if(state == PlanInfo.State.DISCARD)
                    text = "你放弃愿望:" + planInfo.title;
                else if (state == PlanInfo.State.FINISH) {
                    text = "你达成了愿望:" + planInfo.title;
                }
                else
                    text = "what are you so diao?";
                ToastUtil.show(Global.getContext(), text);
                if(callback != null) {
                    BaseRsp rsp = null;
                    try {
                        rsp = Global.objectMapper.readValue(responseString, BaseRsp.class);
                    } catch (IOException e) {
                        TLog.e("changePlanState %s", e);
                    }
                    callback.onSucc(rsp);
                }
            }
        });
    }
    private abstract class PictureUploadRspCallback {
        public abstract void dealSuccResult(final UploadPictureRsp rsp);
    }
}
