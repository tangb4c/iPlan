package me.plan.HomePage.UI;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import com.ecloud.pulltozoomview.PullToZoomListViewEx;
import me.plan.R;
import me.plan.comm.Widget.AutoBgButton;
import me.plan.comm.base.CommonActivity;

/**
 * Created by tangb4c on 2015/2/3.
 */
public class PlanDetailActivity extends CommonActivity implements View.OnClickListener {
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
//        View headView = getLayoutInflater().inflate(R.layout.plan_detail_head_view_layout, null);
//        listView.setHeaderView(headView);
//        View zoomView = getLayoutInflater().inflate(R.layout.plan_detail_head_zoom_view, null);
//        listView.setZoomView(zoomView);
        String[] adapterData = new String[]{"Activity", "Service", "Content Provider", "Intent", "BroadcastReceiver", "ADT", "Sqlite3", "HttpClient",
                "DDMS", "Android Studio", "Fragment", "Loader", "Activity", "Service", "Content Provider", "Intent",
                "BroadcastReceiver", "ADT", "Sqlite3", "HttpClient", "Activity", "Service", "Content Provider", "Intent",
                "BroadcastReceiver", "ADT", "Sqlite3", "HttpClient"};

        //设置header区域的高度 9:24
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, adapterData));
        listView.setBackgroundColor(Color.BLACK);
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
