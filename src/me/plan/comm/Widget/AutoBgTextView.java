package me.plan.comm.Widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;
import me.plan.R;

/**
 * Created by tangb4c on 2015/1/28.
 */
public class AutoBgTextView extends TextView {
    public AutoBgTextView(Context context) {
        super(context);
    }

    public AutoBgTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI(context, attrs);
    }

    public AutoBgTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void initUI(Context context, AttributeSet attrs) {
        if(attrs == null)return;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoBgTextView);
        try {
            AutoPressStateDrawable top = createAutoPressStateDrawable(a, R.styleable.AutoBgTextView_drawableTop);
            AutoPressStateDrawable left = createAutoPressStateDrawable(a, R.styleable.AutoBgTextView_drawableLeft);
            setCompoundDrawablesWithIntrinsicBounds(left, top, null, null);
        } finally {
            a.recycle();
        }
    }
    private AutoPressStateDrawable createAutoPressStateDrawable(TypedArray a, int styleId){
        Drawable drawable = a.getDrawable(styleId);
        if(drawable == null){
            return null;
        }
        AutoPressStateDrawable as = new AutoPressStateDrawable(new Drawable[]{drawable});
        as.attachTextView(this);
        return as;
    }
}
