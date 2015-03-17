package me.plan.HomePage.UI;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import me.plan.R;
import me.plan.comm.Utils;
import me.plan.comm.Widget.AutoPressStateDrawable;

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
/*        TextView loginTextView = (TextView) findViewById(R.id.nav_login);
        Drawable d = getResources().getDrawable(R.drawable.tab_ic_login_default);
        AutoPressStateDrawable as = new AutoPressStateDrawable(new Drawable[]{d});
        loginTextView.setCompoundDrawablesWithIntrinsicBounds(null, as, null, null);*/
    }
}
