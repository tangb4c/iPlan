package me.plan.comm.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import me.plan.R;
import me.plan.comm.Widget.AutoBgButton;
import me.plan.core.TLog;

/**
 * Created by tangb4c on 2015/2/6.
 */
public class CommonActionBarActivity extends ActionBarActivity {
    protected AutoBgButton mLeftTitleBtn;
    protected AutoBgButton mRightTitleBtn;
    protected TextView mMiddleTitleTextView;
    protected void onCreate(Bundle savedInstanceState, int layoutResID) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResID);

        mLeftTitleBtn = (AutoBgButton) findViewById(R.id.left_btn);
        mLeftTitleBtn.setBackgroundResource(R.drawable.nav_ic_tab_default);
        mRightTitleBtn = (AutoBgButton) findViewById(R.id.right_btn);
        mRightTitleBtn.setBackgroundResource(R.drawable.nav_ic_add_default);
        mMiddleTitleTextView = (TextView) findViewById(R.id.middle_title);
    }

    protected void setTextViewById(View root, int resId, String txt) {
        TextView x = (TextView) root.findViewById(resId);
        x.setText(txt);
    }
    protected void setImageViewById(View root, int resId, int drawableId) {
        ImageView x = (ImageView) root.findViewById(resId);
        x.setImageResource(drawableId);
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
}
