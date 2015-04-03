package me.plan.core.UI;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by blake on 3/20/15.
 */
public class ToastUtil {
    public static void show(Context ctx, CharSequence msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }
}
