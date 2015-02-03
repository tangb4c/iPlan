package me.plan.HomePage.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.timessquare.CalendarPickerView;
import me.plan.HomePage.data.BaseRsp;
import me.plan.R;
import me.plan.comm.Utils;
import me.plan.comm.Widget.AutoBgButton;
import me.plan.comm.Widget.AutoBgTextView;
import me.plan.comm.base.CommonActivity;
import me.plan.comm.define.GlobalDef;
import me.plan.comm.define.ParamDef;
import me.plan.core.Global;
import me.plan.core.LoginUser;
import me.plan.core.TLog;
import me.plan.core.network.BlkNetWorker;
import org.apache.http.Header;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tangb4c on 2015/1/30.
 */
public class NewPlanDetailActivity extends CommonActivity implements View.OnClickListener {
    AutoBgTextView mSecretPlan;
    TextView mPlanEndTime;
    Date mPlanEndDate = new Date();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.newplan_detail_layout);
        initUI();
    }

    private void initUI() {

        Intent params = getIntent();
        mLeftTitleBtn.setBackgroundResource(R.drawable.nav_ic_back_default);
        mLeftTitleBtn.setOnClickListener(this);
        mRightTitleBtn.setBackgroundResource(R.drawable.nav_ic_finish_default);
        mRightTitleBtn.setOnClickListener(this);
        mMiddleTitleTextView.setText(params.getStringExtra(CommConst.INTENT_PLAN_TITLE));
//        mPlanTitle = (EditText) findViewById(R.id.plan_title);
        mPlanEndTime = (TextView) findViewById(R.id.plan_endtime);
        mPlanEndTime.setOnClickListener(this);
        mSecretPlan = (AutoBgTextView) findViewById(R.id.secret_plan);
        mSecretPlan.setOnClickListener(this);

/*        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        // init(Date selectedDate, Date minDate, Date maxDate) {...}
        // selectedDate 当前选中日期
        // minDate 对早可选日期 （包含）
        // maxDate 最晚可选日期（不包含）
        // calender.init
        calendar.init(new Date(), new Date(), Locale.CHINA);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.left_btn:
            finish();
            break;
        case R.id.right_btn:
            submitNewPlan();
            break;
        case R.id.secret_plan:
            updateSecretStat();
            break;
        case R.id.plan_endtime:
            showDatePicker();
            break;
        }
    }

    private void updateSecretStat() {
        mSecretPlan.setSelected(!mSecretPlan.isSelected());
        if(mSecretPlan.isSelected()){
            mSecretPlan.setBackgroundColor(getResources().getColor(R.color.secret_color));
        }else{
            mSecretPlan.setBackgroundColor(Color.WHITE);
        }
    }

    private void showDatePicker() {
/*        DatePickerDialog d = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mPlanEndTime.setText(String.format("预计%d-%d-%d达成", year, monthOfYear, dayOfMonth));
            }
        }, 2015, 2, 1);

        d.show();*/
        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        final Calendar lastYear = Calendar.getInstance();
        final CalendarPickerView dialogView = (CalendarPickerView) getLayoutInflater().inflate(R.layout.calendar_dialog, null, false);
        dialogView.init(lastYear.getTime(), nextYear.getTime()) //
                .withSelectedDate(new Date());
        final AlertDialog theDialog =
                new AlertDialog.Builder(this)/*.setTitle("完成日期")*/
                        .setView(dialogView)
                        .setNeutralButton("返回", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("选择", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPlanEndDate = dialogView.getSelectedDate();
                                SimpleDateFormat format = new SimpleDateFormat("预计 yyyy-MM-dd 达成");
                                String result = format.format(mPlanEndDate);
                                mPlanEndTime.setText(result);
                                dialog.dismiss();
                            }
                        })
                        .create();
        theDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                TLog.d("onShow: fix the dimens!");
                dialogView.fixDialogDimens();
            }
        });
        theDialog.show();
    }

    private void submitNewPlan(){
        Intent params = getIntent();
        String title = params.getStringExtra(CommConst.INTENT_PLAN_TITLE);
        if(Utils.isEmpty(title)){
            notifyMessage("获取愿望名字失败");
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.add(ParamDef.OWNERID_STR, LoginUser.g().getUserId());
        requestParams.add(ParamDef.PLAN_TITLE_STR, title);
        requestParams.put(ParamDef.FINISH_DATE_INT, mPlanEndDate.getTime() / 1000);
        requestParams.put(ParamDef.PRIVILEGE_INT, ParamDef.getPrililegeFlagBy(mSecretPlan.isSelected()));
        requestParams.put(ParamDef.BACKGROUND_INDEX_INT, 0);
        requestParams.put(ParamDef.SUBTASK_LIST, "[]");
        BlkNetWorker.g().get(ParamDef.CMD_NEW_PLAN, requestParams, new TextHttpResponseHandler(){
            @Override
            public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
                errLog("添加失败" + responseString);
                dealByRetcode(GlobalDef.ErrorCode.generateNetCode(statusCode), "网络问题");
            }

            @Override
            public void onSuccess(final int statusCode, final Header[] headers, final String responseString) {
                int retcode = 0;
                String errmsg;
                try {
                    BaseRsp rsp = Global.objectMapper.readValue(responseString, BaseRsp.class);
                    retcode = rsp.ret;
                    errmsg = rsp.msg;
                } catch (IOException e) {
                    retcode = GlobalDef.ErrorCode.PARSE_JSON_FAILED;
                    errmsg = "协议解析失败";
                    TLog.e(e.toString());
                }
                dealByRetcode(retcode, errmsg);
            }

            private void dealByRetcode(int retcode, String errmsg) {
                if(retcode < 0){
                    errLog("添加失败 " + errmsg);
                }else{
                    infoLog("添加成功");
                    goNext();
                }
            }
        });
    }
    private void goNext() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}