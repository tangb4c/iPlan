package me.plan.comm.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import me.plan.HomePage.UI.CommConst;
import me.plan.R;
import me.plan.comm.Widget.AutoBgButton;
import me.plan.core.TLog;

/**
 * Created by tangb4c on 2015/1/30.
 */
public abstract class CommonActivity extends Activity {
    protected AutoBgButton mLeftTitleBtn;
    protected AutoBgButton mRightTitleBtn, mRightTitleBtn2;
    protected TextView mMiddleTitleTextView;
    protected void onCreate(Bundle savedInstanceState, int layoutResID) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResID);

        mLeftTitleBtn = (AutoBgButton) findViewById(R.id.left_btn);
//        mLeftTitleBtn.setBackgroundResource(R.drawable.nav_ic_tab_default);
        mRightTitleBtn2 = (AutoBgButton) findViewById(R.id.right_btn_2);
//        mRightTitleBtn2.setBackgroundResource(R.drawable.nav_ic_eidt_default);

        mRightTitleBtn = (AutoBgButton) findViewById(R.id.right_btn);
//        mRightTitleBtn.setBackgroundResource(R.drawable.nav_ic_add_default);
        mMiddleTitleTextView = (TextView) findViewById(R.id.middle_title);
    }

    protected void setTextViewById(View root, int resId, String txt) {
        TextView x = (TextView) root.findViewById(resId);
        x.setText(txt);
    }
    protected void setImageViewById(View root, int resId, int drawableId, View.OnClickListener onClickListener) {
        ImageView x = (ImageView) root.findViewById(resId);
        x.setImageResource(drawableId);
        if(onClickListener != null)
            x.setOnClickListener(onClickListener);
    }
    protected void notifyMessage(String fmt, Object...args) {
        String msg = String.format(fmt, args);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    protected void errLog(String fmt, Object... args) {
        TLog.d(fmt, args);
        notifyMessage(fmt, args);
    }
    protected void infoLog(String fmt, Object... args) {
        TLog.i(fmt, args);
        notifyMessage(fmt, args);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if(requestCode == CommConst.REQUEST_QQ_LOGIN && resultCode == RESULT_OK){
               onCreate(null);
            TLog.i("CommConst.REQUEST_QQ_LOGIN. force refresh");
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
