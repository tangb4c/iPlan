package me.plan.comm;

import me.plan.R;
import me.plan.core.Global;

/**
 * Created by tangb4c on 2015/1/25.
 */
public class Utils {

    static public final int dp2px(float f) {
        return (int) (Global.getDensity() * f);
    }

    static public final int dimen2px(int dimenId) {
        return Global.getContext().getResources().getDimensionPixelSize(dimenId);
    }
    static public boolean isEmpty(String str){
        return str == null || str.isEmpty();
    }
}
