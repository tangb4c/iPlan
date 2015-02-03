package me.plan.HomePage.UI;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import me.plan.HomePage.business.HomePageBusiness;
import me.plan.HomePage.data.PlanInfo;
import me.plan.HomePage.data.PlanListRsp;
import me.plan.R;
import me.plan.comm.Utils;
import me.plan.comm.Widget.AutoBgButton;
import me.plan.comm.base.CommonActivity;
import me.plan.comm.define.ParamDef;
import me.plan.core.Global;
import me.plan.core.LoginUser;
import me.plan.core.TLog;
import me.plan.core.network.BlkNetWorker;
import org.apache.http.Header;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.SimpleFormatter;

public class MainActivity extends CommonActivity implements View.OnClickListener {
    NavMenuWidget mNavMenu;
    ViewPager mCardListPager;
    CardListPagerAdapter mCardAdapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.main);
//        Debug.waitForDebugger();
        initData();
        initUI();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();
    }

    private void initData() {
        RequestParams requestParams = new RequestParams();
        requestParams.add(ParamDef.USERID, LoginUser.g().getUserId());
        BlkNetWorker.g().get(ParamDef.CMD_GET_PLAN_LIST, requestParams, new TextHttpResponseHandler() {

            @Override
            public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
                TLog.e("拉取列表失败, statusCode:%d response:%s exception:%s", statusCode, responseString, throwable);
            }

            @Override
            public void onSuccess(final int statusCode, final Header[] headers, final String responseString) {
                try {
//                    Debug.waitForDebugger();
                    PlanListRsp rsp = Global.objectMapper.readValue(responseString, PlanListRsp.class);
                    if(rsp.data == null)
                        mCardAdapter.planInfos.clear();
                    else
                        mCardAdapter.planInfos = rsp.data;

                    mCardAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initUI() {
        //init common
        mLeftTitleBtn.setBackgroundResource(R.drawable.nav_ic_tab_default);
//        mLeftTitleBtn.setBackgroundResource(R.drawable.back_icon);
        mLeftTitleBtn.setOnClickListener(this);
        mRightTitleBtn.setBackgroundResource(R.drawable.nav_ic_add_default);
        mRightTitleBtn.setOnClickListener(this);

        mNavMenu = new NavMenuWidget(this);

        mCardListPager = (ViewPager) findViewById(R.id.cardlist);
        mCardAdapter = new CardListPagerAdapter();
        mCardListPager.setAdapter(mCardAdapter);
        mCardListPager.setOffscreenPageLimit(3);
        mCardListPager.setPageMargin(Utils.dimen2px(R.dimen.main_cardlist_gap));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.left_btn:
            mNavMenu.showMenu();
            break;
        case R.id.right_btn:
            gotoAddPlan();
            break;
        case R.id.btn_ic_more:
            showIcMoreDialog();
            break;
        default:
        }
    }

    private void gotoAddPlan() {
        Intent intent = new Intent(this, NewPlanActivity.class);
        startActivity(intent);
    }

    private void showIcMoreDialog() {
        final Dialog dialog = new Dialog(this, R.style.popupDialog);

        dialog.setContentView(R.layout.card_operation_dialog);
        //full screen
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);

        dialog.setCanceledOnTouchOutside(true);
        View dialogClose = dialog.findViewById(R.id.card_dialog_close);
        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    class CardListPagerAdapter extends PagerAdapter{
        ArrayList<PlanInfo> planInfos = new ArrayList<PlanInfo>();
        public CardListPagerAdapter(){
//            TLog.tag();
/*            for(int i = 0; i < 5; ++i){
                PlanInfo planInfo = new PlanInfo();
                planInfos.add(planInfo);
            }*/
        }
        @Override
        public int getCount() {
//            TLog.i("planInfos null:%b", planInfos == null);
            return planInfos.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            return super.instantiateItem(container, position);
            PlanInfo planInfo = planInfos.get(position);
            final View rootView = getLayoutInflater().inflate(R.layout.card_item, null);
            int imgId = 0;
            Random random = new Random();
            int r = Global.random.nextInt(3);
            if(r == 0)
                imgId = R.drawable.cover1;
            else if(r == 1)
                imgId = R.drawable.cover2;
            else
                imgId = R.drawable.cover3;

            setImageViewById(rootView, R.id.card_item_cover, imgId);
            setTextViewById(rootView, R.id.card_item_recodtime, new SimpleDateFormat("最后记录:yyyy-MM-dd").format(planInfo.getRecodDate()));
            setTextViewById(rootView, R.id.card_item_title, planInfo.title);
            setTextViewById(rootView, R.id.card_item_brief, String.format("已留下%d个努力瞬间", planInfo.tryTimes));
            setTextViewById(rootView, R.id.card_item_pastdays, planInfo.getPastDays() + "");
            setTextViewById(rootView, R.id.card_item_totaldays, "/" + planInfo.getTotalDays());
            AutoBgButton btnIcMore = (AutoBgButton) rootView.findViewById(R.id.btn_ic_more);
            btnIcMore.setOnClickListener(MainActivity.this);
            ViewPager v = (ViewPager) container;
            v.addView(rootView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ViewPager v = (ViewPager) container;
            v.removeView((View) object);
        }
    }
}
