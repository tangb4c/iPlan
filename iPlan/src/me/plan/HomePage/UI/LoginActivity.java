package me.plan.HomePage.UI;

import android.os.Bundle;
import com.tencent.tauth.Tencent;
import me.plan.R;
import me.plan.comm.base.CommonActivity;
import me.plan.core.Global;

/**
 * Created by tangb4c on 2015/3/17.
 */
public class LoginActivity extends CommonActivity {
    Tencent mTencent;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.plan_login_layout);
        mTencent = Tencent.createInstance(getResources().getString(R.string.qq_appid), Global.getContext());
    }
}
