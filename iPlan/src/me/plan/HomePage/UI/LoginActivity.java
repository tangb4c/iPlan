package me.plan.HomePage.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import me.plan.HomePage.data.GetUidRsp;
import me.plan.R;
import me.plan.comm.Utils;
import me.plan.comm.base.CommonActivity;
import me.plan.comm.define.GlobalDef;
import me.plan.comm.define.ParamDef;
import me.plan.core.Global;
import me.plan.core.LoginUser;
import me.plan.core.TLog;
import me.plan.core.UI.ToastUtil;
import me.plan.core.network.BlkNetWorker;
import org.apache.http.Header;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by tangb4c on 2015/3/17.
 */
public class LoginActivity extends CommonActivity {
    boolean isShowQQLogin = false;
    Tencent mTencent;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.plan_login_layout);
        mTencent = Tencent.createInstance(getString(R.string.qq_appid), Global.getContext());
        if (mTencent == null) {
            TLog.e("mTencent is null");
            return;
        }else{
            loadQQToken();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isShowQQLogin) {
            if (mTencent.isSessionValid()) {
                ToastUtil.show(this, "登录成功");
                swapLoginToken();
            } else {
                mTencent.login(this, "get_user_info", mLoginListener);
            }
            isShowQQLogin = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mTencent != null) {
            mTencent.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 换取登录UID
     */
    protected void swapLoginToken() {
        RequestParams requestParams = new RequestParams();
        requestParams.add(ParamDef.OPENID, mTencent.getOpenId());
        requestParams.add(ParamDef.TOKEN, mTencent.getAccessToken());
        requestParams.put(ParamDef.LOGIN_TYPE, "qq");
        BlkNetWorker.g().get_with_notoken(ParamDef.CMD_GET_UID, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
                errLog("登录UID失败, %d %s", GlobalDef.ErrorCode.generateNetCode(statusCode), "网络问题");
                setResult(RESULT_CANCELED);
                finish();
            }

            @Override
            public void onSuccess(final int statusCode, final Header[] headers, final String responseString) {
                int retcode = 0;
                String errmsg;
                try {
                    GetUidRsp rsp = Global.objectMapper.readValue(responseString, GetUidRsp.class);
                    retcode = rsp.ret;
                    errmsg = rsp.msg;
                    if (retcode < 0 || rsp.data == null) {
                        errLog("获取UID失败: " + errmsg);
                    } else {
                        LoginUser.g().setUserId(rsp.data.uid);
                        LoginUser.g().setUkey(rsp.data.ukey);
                        infoLog("登录成功");
                        TLog.i("uid:%s ukey:%s", rsp.data.uid, rsp.data.ukey);
                        setResult(RESULT_OK);
                    }
                } catch (IOException e) {
                    retcode = GlobalDef.ErrorCode.PARSE_JSON_FAILED;
                    errmsg = "协议解析失败";
                    TLog.e("ret:%d %s exception:%s", retcode, errmsg, e.toString());
                    setResult(RESULT_CANCELED);
                } finally {
                    finish();
                }

            }
        });
    }

    protected void initOpenidAndToken(Object o) {
        TLog.i("initOpenidAndToken by:" + o);
        try {
            JSONObject jsonObject = new JSONObject(o.toString());
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
                saveQQToken(openId, token, expires);
            }
            swapLoginToken();
        } catch (Exception e) {
            errLog("initOpenidFailed.%s", e);
            finish();
        }
    }
    protected void saveQQToken(String openId, String token, String expiredTime) {
        SharedPreferences sharedPreferences = getSharedPreferences(GlobalDef.SP.LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GlobalDef.LOGIN.OPENID, openId);
        editor.putString(GlobalDef.LOGIN.QQTOKEN, token);
        editor.putString(GlobalDef.LOGIN.EXPIREDTIME, expiredTime);
        editor.apply();
        TLog.e("%s %s %s", openId, token, expiredTime);
    }
    protected void loadQQToken(){
        SharedPreferences sharedPreferences = getSharedPreferences(GlobalDef.SP.LOGIN, Context.MODE_PRIVATE);
        final String openId = sharedPreferences.getString(GlobalDef.LOGIN.OPENID, "");
        final String token = sharedPreferences.getString(GlobalDef.LOGIN.QQTOKEN, "");
        final String expiredTime = sharedPreferences.getString(GlobalDef.LOGIN.EXPIREDTIME, "");

        TLog.e("%s %s %s", openId, token, expiredTime);
        if(Utils.isEmpty(openId) && !Utils.isEmpty(token) && !Utils.isEmpty(expiredTime)) {
            mTencent.setOpenId(openId);
            mTencent.setAccessToken(token, expiredTime);
        }
    }
    IUiListener mLoginListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            initOpenidAndToken(o);
        }

        @Override
        public void onError(UiError uiError) {
            ToastUtil.show(LoginActivity.this, "login error:" + uiError.errorDetail);
            LoginActivity.this.finish();
        }

        @Override
        public void onCancel() {
            ToastUtil.show(LoginActivity.this, "取消登录");
            LoginActivity.this.finish();
        }
    };
}
