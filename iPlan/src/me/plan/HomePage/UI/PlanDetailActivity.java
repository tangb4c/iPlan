package me.plan.HomePage.UI;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.ecloud.pulltozoomview.PullToZoomListViewEx;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;
import me.plan.HomePage.adpater.PlanDetailListAdapter;
import me.plan.HomePage.data.PlanFeedListRsp;
import me.plan.HomePage.data.PlanListRsp;
import me.plan.HomePage.data.element.FeedItem;
import me.plan.HomePage.data.element.PlanDetailInfo;
import me.plan.HomePage.data.element.PlanInfo;
import me.plan.R;
import me.plan.comm.ImageUtils;
import me.plan.comm.Utils;
import me.plan.comm.Widget.AutoBgButton;
import me.plan.comm.base.CommonActivity;
import me.plan.comm.busi.OperationCallback;
import me.plan.comm.define.ParamDef;
import me.plan.core.Global;
import me.plan.core.TLog;
import me.plan.core.UI.ToastUtil;
import me.plan.core.network.BlkNetWorker;
import org.apache.http.Header;

import java.io.IOException;

/**
 * Created by tangb4c on 2015/2/3.
 */
public class PlanDetailActivity extends CommonActivity implements View.OnClickListener {
    private static final int REQUEST_MODIFY_PLAN = 101;
    PlanDetailListAdapter planDetailListAdapter;
    CameraTakePic mCameraTake;
    PlanDetailInfo mPlanDetailInfo;
    View mHeadView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.plan_detail_layout);
        mLeftTitleBtn.setBackgroundResource(R.drawable.nav_ic_back_default);
        mLeftTitleBtn.setOnClickListener(this);
        mRightTitleBtn.setBackgroundResource(R.drawable.nav_ic_share_default);
        mRightTitleBtn.setOnClickListener(this);
        mRightTitleBtn2.setBackgroundResource(R.drawable.nav_ic_eidt_default);
        mRightTitleBtn2.setVisibility(View.VISIBLE);
        mRightTitleBtn2.setOnClickListener(this);

        PullToZoomListViewEx listView = (PullToZoomListViewEx) findViewById(R.id.planlistview);
        View emptyView = getLayoutInflater().inflate(R.layout.plan_detail_empty_view, null);
        listView.setBackgroundDrawable(ImageUtils.BoxBlurFilter(R.drawable.cover2));
        listView.setZoomView(getLayoutInflater().inflate(R.layout.plan_detail_head_zoom_view, null));
        planDetailListAdapter = new PlanDetailListAdapter(Global.getContext(), R.layout.plan_detail_item);
        listView.setAdapter(planDetailListAdapter);
//        listView.setBackgroundColor(Color.TRANSPARENT);
        mHeadView = listView.getHeaderView();

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingBtn);
        floatingActionButton.attachToListView(listView.getRootView(), new ScrollDirectionListener() {
            @Override
            public void onScrollDown() {
                TLog.tag();
            }

            @Override
            public void onScrollUp() {
                TLog.tag();
            }
        });
        floatingActionButton.setOnClickListener(this);
        //设置header区域的高度 9:24
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 24.0F)));
        listView.setHeaderLayoutParams(localObject);
        getPlanDetailList(null);
    }

    private void getPlanDetailList(final String attachInfo) {
        Intent intent = getIntent();
        final String planId = intent.getStringExtra(CommConst.INTENT_PLAN_ID);
        RequestParams requestParams = new RequestParams();
        requestParams.add(ParamDef.ID, planId);
        if (!Utils.isEmpty(attachInfo))
            requestParams.add(ParamDef.ATTACHINFO, attachInfo);
        BlkNetWorker.g().get(ParamDef.CMD_GET_PLAN_FEEDLIST, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
                TLog.e("拉取feeds列表失败, statusCode:%d response:%s exception:%s", statusCode, responseString, throwable);
            }

            @Override
            public void onSuccess(final int statusCode, final Header[] headers, final String responseString) {
                try {
                    TLog.e(responseString);
                    PlanFeedListRsp rsp = Global.objectMapper.readValue(responseString, PlanFeedListRsp.class);
                    dealPlanFeedList(rsp);
                } catch (Exception e) {
                    TLog.e("getPlanDetailList failed. %s", e.toString());
                }
            }
        });
    }

    private void dealPlanFeedList(PlanFeedListRsp rsp) {
        mPlanDetailInfo = rsp.data;
        if (rsp == null || rsp.data == null || rsp.data.feedsList == null) {
            TLog.i("some one is null. skip dealPlanFeedList");
            return;
        }
        PlanInfo planInfo = mPlanDetailInfo.plan;
        setTextViewById(mHeadView, R.id.plan_title, planInfo.title);
        setTextViewById(mHeadView, R.id.plan_content, " ");
        TLog.i("add feedsList size:%d", rsp.data.feedsList.size());
        planDetailListAdapter.addAll(rsp.data.feedsList);
        planDetailListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
        case R.id.left_btn:
            finish();
            break;
        case R.id.right_btn:
            break;
        case R.id.right_btn_2:
            gotoEditPlan();
            break;
        case R.id.floatingBtn:
            if (mPlanDetailInfo != null) {
                if (mCameraTake == null)
                    mCameraTake = new CameraTakePic();
                mCameraTake.dispatchTakePictureIntent(this, mPlanDetailInfo.plan, feedItemOperationCallback);
            }else{
                ToastUtil.show(this, "没有相关列表数据");
            }
            break;
        }
    }

    private void gotoEditPlan() {
        NewPlanActivity.startPlanForResult(this, true, mPlanDetailInfo.plan);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCameraTake != null)
            mCameraTake.dealOnActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MODIFY_PLAN && resultCode == RESULT_OK) {
            onCreate(null);
        }
    }
    OperationCallback<FeedItem> feedItemOperationCallback = new OperationCallback<FeedItem>() {
        @Override
        public void onSucc(FeedItem item) {
            planDetailListAdapter.add(item);
            planDetailListAdapter.notifyDataSetChanged();
        }
    };
}
