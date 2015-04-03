package me.plan.comm;

import android.content.Intent;
import android.os.Bundle;
import me.plan.R;
import me.plan.core.Global;
import me.plan.core.TLog;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    static public boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    static public String bundleToString(final Bundle b) {
        if (b == null)
            return "null";

        StringBuilder sb = new StringBuilder(b.size() * 20);
        for (String key : b.keySet()) {
            sb.append(key).append(":").append(b.get(key)).append(",");
        }
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    static public String intentToString(Intent intent) {
        if (intent == null || intent.getExtras() == null)
            return "null";
        final Bundle b = intent.getExtras();
        if (b != null && !b.isEmpty()) {
            String extrasDetail = bundleToString(intent.getExtras());
            return String.format("%s (extras:%s)", intent.toString(), extrasDetail);
        } else {
            return intent.toString();
        }

    }

    public static String getShortDate(int time) {
        Date date = new Date(time * 1000L);
        TLog.i("date:%s time:%d", date, time);
        final SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        return sd.format(date);
    }
    public static int getTimeStamp(){
        return (int)(new Date().getTime()/1000);
    }
}
