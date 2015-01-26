package me.plan.HomePage.UI;

import android.app.Activity;
import android.content.Context;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import me.plan.R;
import me.plan.comm.Utils;

/**
 * Created by tangb4c on 2015/1/25.
 */
public class NavMenuWidget extends SlidingMenu {
    Activity activity;

    public NavMenuWidget(Activity context) {
        super(context);
        activity = context;
        init();
    }

    private void init() {
        setMode(LEFT);
        setTouchModeAbove(TOUCHMODE_MARGIN);
        setBehindWidth(Utils.dimen2px(R.dimen.main_nav_width));
        attachToActivity(activity, SLIDING_WINDOW);
        setMenu(R.layout.navigate_menu);
    }
}
