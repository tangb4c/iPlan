package me.plan.HomePage.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import me.plan.R;
import me.plan.comm.Utils;
import me.plan.comm.Widget.AutoBgButton;
import me.plan.comm.base.CommonActivity;
import me.plan.core.TLog;

/**
 * Created by tangb4c on 2015/1/30.
 */
public class NewPlanActivity extends CommonActivity implements View.OnClickListener {
    EditText mPlanTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.newplan_layout);
        TLog.tag();
        initUI();
    }

    private void initUI() {
        mLeftTitleBtn.setBackgroundResource(R.drawable.nav_ic_back_default);
        mLeftTitleBtn.setOnClickListener(this);
        mRightTitleBtn.setBackgroundResource(R.drawable.nav_ic_finish_default);
        mRightTitleBtn.setOnClickListener(this);
        mPlanTitle = (EditText) findViewById(R.id.plan_title);
        AutoBgButton titleA = (AutoBgButton) findViewById(R.id.template_title_a);
        AutoBgButton titleB = (AutoBgButton) findViewById(R.id.template_title_b);
        titleA.setOnClickListener(this);
        titleB.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.left_btn:
            finish();
            break;
        case R.id.right_btn:
            goNext();
            break;
        case R.id.template_title_a:
        case R.id.template_title_b:
            mPlanTitle.setText(((AutoBgButton)v).getText());
            break;
        }
    }

    private void goNext() {
        String title = mPlanTitle.getText().toString();
        if (Utils.isEmpty(title)) {
            notifyMessage("愿望不能为空啊，同学");
            return;
        }
        Intent intent = new Intent(this, NewPlanDetailActivity.class);
        intent.putExtra(CommConst.INTENT_PLAN_TITLE, title);
        startActivity(intent);
    }
}
