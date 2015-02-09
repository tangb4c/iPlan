package me.plan.HomePage.UI;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.ecloud.pulltozoomview.PullToZoomListViewEx;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;
import me.plan.HomePage.adpater.PlanDetailListAdapter;
import me.plan.R;
import me.plan.comm.ImageUtils;
import me.plan.comm.Widget.AutoBgButton;
import me.plan.comm.base.CommonActivity;
import me.plan.core.Global;
import me.plan.core.TLog;

/**
 * Created by tangb4c on 2015/2/3.
 */
public class PlanDetailActivity extends CommonActivity implements View.OnClickListener {
    PlanDetailListAdapter planDetailListAdapter = new PlanDetailListAdapter(Global.getContext(), R.layout.plan_detail_item);
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.plan_detail_layout);
        mLeftTitleBtn.setBackgroundResource(R.drawable.nav_ic_back_default);
        mLeftTitleBtn.setOnClickListener(this);
        mRightTitleBtn.setBackgroundResource(R.drawable.nav_ic_share_default);
        mRightTitleBtn.setOnClickListener(this);
        mRightTitleBtn2.setBackgroundResource(R.drawable.nav_ic_eidt_default);
        mRightTitleBtn2.setVisibility(View.VISIBLE);


        PullToZoomListViewEx listView = (PullToZoomListViewEx) findViewById(R.id.planlistview);
        View emptyView = getLayoutInflater().inflate(R.layout.plan_detail_empty_view, null);
        listView.setBackgroundDrawable(ImageUtils.BoxBlurFilter(R.drawable.cover2));
        listView.setZoomView(getLayoutInflater().inflate(R.layout.plan_detail_head_zoom_view, null));
        listView.setAdapter(planDetailListAdapter);
//        listView.setBackgroundColor(Color.TRANSPARENT);

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
        //设置header区域的高度 9:24
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 24.0F)));
        listView.setHeaderLayoutParams(localObject);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
        case R.id.left_btn:
            finish();
            break;
        case R.id.right_btn:
            break;
        }
    }
}
