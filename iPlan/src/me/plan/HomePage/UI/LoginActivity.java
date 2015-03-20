package me.plan.HomePage.UI;

import android.content.Intent;
import android.os.Bundle;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import me.plan.R;
import me.plan.comm.base.CommonActivity;
import me.plan.core.Global;
import me.plan.core.TLog;
import me.plan.core.UI.ToastUtil;

/**
 * Created by tangb4c on 2015/3/17.
 */
public class LoginActivity extends CommonActivity {
    Tencent mTencent;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mTencent != null) {
            mTencent.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.plan_login_layout);
        mTencent = Tencent.createInstance(getString(R.string.qq_appid), Global.getContext());
        if (mTencent == null) {
            TLog.e("mTencent is null");
            return;
        }
        if (mTencent.isSessionValid()) {
            ToastUtil.show(this, "登录成功");
            QQToken qqToken = mTencent.getQQToken();
        } else {
            mTencent.login(this, "iPlan Login", mLoginListener);
        }
    }

    IUiListener mLoginListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            TLog.i("onComplete" + o);
            Global.objectMapper;
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    };
}
