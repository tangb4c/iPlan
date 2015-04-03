package me.plan.HomePage.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import me.plan.HomePage.data.element.PlanInfo;
import me.plan.R;
import me.plan.comm.Utils;
import me.plan.comm.Widget.AutoBgButton;
import me.plan.comm.base.CommonActivity;
import me.plan.core.TLog;

/**
 * Created by tangb4c on 2015/1/30.
 */
public class NewPlanActivity extends CommonActivity implements View.OnClickListener {
    private static final int REQUEST_PLAN_2 = 100;
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

        Intent intent = getIntent();
        TLog.i(Utils.intentToString(intent));
        if (intent.getIntExtra(CommConst.INTENT_PLAN_MODE, 0) == CommConst.INTENT_PLAN_MODE_MODIFY) {
            String t = intent.getStringExtra(CommConst.INTENT_PLAN_TITLE);
            mPlanTitle.setText(t);
        }
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
            mPlanTitle.setText(((AutoBgButton) v).getText());
            break;
        }
    }

    private void goNext() {
        String title = mPlanTitle.getText().toString();
        if (Utils.isEmpty(title)) {
            notifyMessage("愿望不能为空啊，同学");
            return;
        }
        Intent params = getIntent();

        Intent intent = new Intent(this, NewPlanDetailActivity.class);
        intent.putExtras(params.getExtras());
        intent.putExtra(CommConst.INTENT_PLAN_TITLE, title);
        startActivityForResult(intent, REQUEST_PLAN_2);
    }

    public static void startPlanForResult(Activity activity, boolean edit, PlanInfo planInfo) {
        Intent intent = new Intent(activity, NewPlanActivity.class);
        if (edit)
            intent.putExtra(CommConst.INTENT_PLAN_MODE, CommConst.INTENT_PLAN_MODE_MODIFY);
        if(planInfo != null) {
            intent.putExtra(CommConst.INTENT_PLAN_ID, planInfo.id);
            intent.putExtra(CommConst.INTENT_PLAN_TITLE, planInfo.title);
        }
        activity.startActivityForResult(intent, CommConst.REQUEST_MODIFY_PLAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PLAN_2 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
